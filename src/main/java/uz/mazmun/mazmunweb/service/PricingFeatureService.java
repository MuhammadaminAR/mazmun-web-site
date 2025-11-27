package uz.mazmun.mazmunweb.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.mazmun.mazmunweb.dto.PricingFeatureDto;
import uz.mazmun.mazmunweb.entity.PricingFeature;
import uz.mazmun.mazmunweb.repo.PricingFeatureRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PricingFeatureService {

    private final PricingFeatureRepository featureRepository;

    public List<PricingFeatureDto> getAll() {
        return featureRepository.findAllByOrderByCategoryNameAscDisplayOrderAsc()
                .stream()
                .map(this::toDto)
                .toList();
    }

    public PricingFeatureDto create(PricingFeatureDto dto) {
        PricingFeature feature = new PricingFeature();
        feature.setCode(dto.getCode());
        feature.setTitle(dto.getTitle());
        feature.setCategoryName(dto.getCategoryName());
        feature.setDisplayOrder(dto.getDisplayOrder());
        feature.setBasePriceAmount(dto.getBasePriceAmount());
        feature.setBasePriceUnit(dto.getBasePriceUnit());
        return toDto(featureRepository.save(feature));
    }

    public PricingFeatureDto update(Long id, PricingFeatureDto dto) {
        PricingFeature feature = featureRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Feature not found: " + id));

        feature.setCode(dto.getCode());
        feature.setTitle(dto.getTitle());
        feature.setCategoryName(dto.getCategoryName());
        feature.setDisplayOrder(dto.getDisplayOrder());
        feature.setBasePriceAmount(dto.getBasePriceAmount());
        feature.setBasePriceUnit(dto.getBasePriceUnit());

        return toDto(feature);
    }

    public void delete(Long id) {
        featureRepository.deleteById(id);
    }

    private PricingFeatureDto toDto(PricingFeature f) {
        PricingFeatureDto dto = new PricingFeatureDto();
        dto.setId(f.getId());
        dto.setCode(f.getCode());
        dto.setTitle(f.getTitle());
        dto.setCategoryName(f.getCategoryName());
        dto.setDisplayOrder(f.getDisplayOrder());
        dto.setBasePriceAmount(f.getBasePriceAmount());
        dto.setBasePriceUnit(f.getBasePriceUnit());
        return dto;
    }
}
