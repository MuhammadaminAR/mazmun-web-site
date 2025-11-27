package uz.mazmun.mazmunweb.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.mazmun.mazmunweb.dto.PlanFeatureAvailabilityDto;
import uz.mazmun.mazmunweb.dto.PricingPlanResponseDto;
import uz.mazmun.mazmunweb.dto.PricingPlanUpsertDto;
import uz.mazmun.mazmunweb.entity.PricingFeature;
import uz.mazmun.mazmunweb.entity.PricingPlan;
import uz.mazmun.mazmunweb.entity.PricingPlanFeature;
import uz.mazmun.mazmunweb.repo.PricingFeatureRepository;
import uz.mazmun.mazmunweb.repo.PricingPlanFeatureRepository;
import uz.mazmun.mazmunweb.repo.PricingPlanRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PricingPlanService {

    private final PricingPlanRepository planRepository;
    private final PricingFeatureRepository featureRepository;
    private final PricingPlanFeatureRepository mappingRepository;

    /* --------- READ --------- */

    @Transactional(readOnly = true)
    public List<PricingPlanResponseDto> getAllForAdmin() {
        return planRepository.findAll().stream()
                .sorted(Comparator.comparing(p ->
                        Optional.ofNullable(p.getDisplayOrder()).orElse(999)))
                .map(this::toDtoWithFeatures)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<PricingPlanResponseDto> getActivePlans() {
        return planRepository.findAllByActiveTrueOrderByDisplayOrderAsc().stream()
                .map(this::toDtoWithFeatures)
                .toList();
    }

    /* --------- CREATE / UPDATE / DELETE --------- */

    @Transactional
    public PricingPlanResponseDto createPlan(PricingPlanUpsertDto dto) {
        try {
            PricingPlan plan = new PricingPlan();
            applyPlanFields(plan, dto);

            syncPlanFeatures(plan, dto.getFeatures());
            planRepository.save(plan);
            return toDtoWithFeatures(plan);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void deletePlan(Long id) {
        // birinchi mappinglarni o‘chiramiz, keyin plan’ni
        mappingRepository.deleteByPlanId(id);
        planRepository.deleteById(id);
    }


    public PricingPlanResponseDto updatePlan(Long id, PricingPlanUpsertDto dto) {
        PricingPlan plan = planRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Plan not found: " + id));

        applyPlanFields(plan, dto);
        syncPlanFeatures(plan, dto.getFeatures());
        planRepository.save(plan);

        return toDtoWithFeatures(plan);
    }

    /* --------- HELPERS --------- */

    private void applyPlanFields(PricingPlan plan, PricingPlanUpsertDto dto) {
        plan.setName(dto.getName());
        plan.setSlug(dto.getSlug());
        plan.setCode(dto.getCode());
        plan.setTagline(dto.getTagline());
        plan.setPriceAmount(dto.getPriceAmount());
        plan.setPriceUnit(dto.getPriceUnit());
        plan.setDisplayOrder(dto.getDisplayOrder());
        plan.setPopular(dto.getPopular());
        plan.setCustom(dto.getCustom());
        plan.setActive(dto.getActive());
    }

    private void syncPlanFeatures(PricingPlan plan, List<PlanFeatureAvailabilityDto> featureDtos) {

        if (plan.getFeatureMappings() != null) {
            plan.getFeatureMappings().forEach(feature -> feature.setPlan(null));
            plan.getFeatureMappings().clear();
            planRepository.save(plan);
        } else {
            plan.setFeatureMappings(new ArrayList<>());
        }

        // 2) Yangi hech narsa kelmagan bo‘lsa – shunchaki bo‘sh qoldiramiz
        if (featureDtos == null || featureDtos.isEmpty()) {
            return;
        }

        // Kerakli feature’larni bitta query bilan olib kelamiz
        Map<Long, PricingFeature> featureMap = featureRepository.findAllById(
                        featureDtos.stream()
                                .map(PlanFeatureAvailabilityDto::getFeatureId)
                                .collect(Collectors.toSet())
                ).stream()
                .collect(Collectors.toMap(PricingFeature::getId, f -> f));

        List<PricingPlanFeature> mappings = new ArrayList<>();

        for (PlanFeatureAvailabilityDto fdto : featureDtos) {
            PricingFeature feature = featureMap.get(fdto.getFeatureId());
            if (feature == null) {
                throw new IllegalArgumentException(
                        "Feature not found in DB. ID = " + fdto.getFeatureId()
                                + " | DTO list size=" + featureDtos.size()
                                + " | FEATURES IN MAP: " + featureMap.keySet()
                );
            }

            PricingPlanFeature mapping = PricingPlanFeature.builder()
                    .plan(plan)
                    .feature(feature)
                    .availability(fdto.getAvailability())
                    .note(fdto.getNote())
                    .build();

            plan.getFeatureMappings().add(mapping);     // parent collection’ga qo‘shamiz

        }
    }

    private PricingPlanResponseDto toDtoWithFeatures(PricingPlan plan) {
        PricingPlanResponseDto dto = new PricingPlanResponseDto();
        dto.setId(plan.getId());
        dto.setName(plan.getName());
        dto.setSlug(plan.getSlug());
        dto.setCode(plan.getCode());
        dto.setTagline(plan.getTagline());
        dto.setPriceAmount(plan.getPriceAmount());
        dto.setPriceUnit(plan.getPriceUnit());
        dto.setDisplayOrder(plan.getDisplayOrder());
        dto.setPopular(plan.isPopular());
        dto.setCustom(plan.isCustom());
        dto.setActive(plan.isActive());
        try {

            List<PricingPlanFeature> mappings = mappingRepository.findByPlanId(plan.getId());
            List<PlanFeatureAvailabilityDto> featureDtos = mappings.stream().map(m -> {
                PlanFeatureAvailabilityDto f = new PlanFeatureAvailabilityDto();
                f.setFeatureId(m.getFeature().getId());
                f.setAvailability(m.getAvailability());
                f.setNote(m.getNote());
                return f;
            }).toList();
            dto.setFeatures(featureDtos);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return dto;
    }

}
