package uz.mazmun.mazmunweb.service;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import uz.mazmun.mazmunweb.dto.HomePageVideoDTO;
import uz.mazmun.mazmunweb.entity.HomePageVideo;
import uz.mazmun.mazmunweb.repo.HomePageVideoRepository;

import java.net.URI;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class HomePageVideoService {

    private final HomePageVideoRepository repo;

    public HomePageVideoService(HomePageVideoRepository repo){ this.repo = repo; }

    /** Public/Admin GET: joriy sozlama */
    public HomePageVideoDTO get(){
        HomePageVideo e = repo.findTopByOrderByIdAsc().orElse(null);
        return HomePageVideoDTO.from(e);
    }

    /** Admin PUT: YouTube linkni saqlash yoki null bilan tozalash */
    public HomePageVideoDTO put(String rawUrl){
        // Mavjud bitta yozuvni olamiz yoki yangisini yaratamiz (id qo‘ymaymiz!)
        HomePageVideo e = repo.findTopByOrderByIdAsc().orElseGet(HomePageVideo::new);

        if (!StringUtils.hasText(rawUrl)) {
            // Tozalash (fallback rasm)
            e.setUrl(null);
            e.setEmbedId(null);
            return HomePageVideoDTO.from(repo.save(e)); // yangi bo‘lsa INSERT, aks holda UPDATE
        }

        // Faqat YouTube’ga ruxsat
        String low = rawUrl.toLowerCase();
        if (!(low.contains("youtube.com") || low.contains("youtu.be"))) {
            throw new IllegalArgumentException("Only YouTube links are allowed");
        }

        // YouTube video ID ni ajratish
        String id = extractYouTubeId(rawUrl);
        if (!StringUtils.hasText(id)) {
            throw new IllegalArgumentException("Invalid YouTube URL");
        }

        e.setUrl(rawUrl.trim());
        e.setEmbedId(id);

        return HomePageVideoDTO.from(repo.save(e));
    }

    /* ——— URL → videoID ——— */
    static String extractYouTubeId(String url){
        try{
            URI u = URI.create(url.trim());
            String host = (u.getHost()==null? "" : u.getHost()).toLowerCase();

            // youtu.be/VIDEOID
            if (host.contains("youtu.be")) {
                String p = u.getPath(); // /VIDEOID
                if (p != null && p.length() > 1) return p.substring(1);
            }

            // watch?v=VIDEOID
            String q = u.getQuery();
            if (q != null) {
                for (String pair : q.split("&")) {
                    String[] kv = pair.split("=");
                    if (kv.length == 2 && kv[0].equals("v")) return kv[1];
                }
            }

            // /embed/VIDEOID yoki /shorts/VIDEOID
            String path = u.getPath()==null? "" : u.getPath();
            Matcher m = Pattern.compile("/(embed|shorts)/([^/?#]+)").matcher(path);
            if (m.find()) return m.group(2);

            return null;
        }catch(Exception e){ return null; }
    }
}
