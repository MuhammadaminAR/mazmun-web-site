package uz.mazmun.mazmunweb.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

    @GetMapping("/")
    public String home(){
        return "forward:/frontend/home-page.html";
    }

    @GetMapping("/home")
    public String legacyHomeRedirect() {
        return "redirect:/";
    }
    @GetMapping("/home/")
    public String legacyHomeRedirect2() {
        return "redirect:/";
    }
    @GetMapping("/admin-user/login")
    public String login(){ return "forward:/frontend/login.html"; }

    @GetMapping("/about")
    public String about(){ return "forward:/frontend/about.html"; }

    @GetMapping({"/admin", "/admin/"})
    public String admin(){ return "forward:/frontend/admin.html"; }

    @GetMapping("/admin/messages")
    public String adminMessages(){ return "forward:/frontend/user-messages.html"; }

    @GetMapping({"/super-admin", "/super-admin/"})
    public String superAdmin(){ return "forward:/frontend/superadmin.html"; }

    @GetMapping("/contact")
    public String contact(){ return "forward:/frontend/contact.html"; }

    @GetMapping("/terms")
    public String terms(){ return "forward:/frontend/terms.html"; }

    @GetMapping("/privacy")
    public String privacy(){ return "forward:/frontend/privacy.html"; }

    @GetMapping("/post")
    public String post(){ return "redirect:/posts"; } // templates/post.html

    @GetMapping("/posts")
    public String posts(){ return "forward:/frontend/posts.html"; }

    @GetMapping("/project")
    public String project(){ return "forward:/frontend/project.html"; }

    @GetMapping("/projects")
    public String projects(){ return "forward:/frontend/projects.html"; }

    @GetMapping("/service")
    public String service(){ return "forward:/frontend/service.html"; }

    @GetMapping("/services")
    public String services(){ return "forward:/frontend/services.html"; }

    @GetMapping("/error/404")
    public String error404(){ return "forward:/frontend/404.html"; }

    @GetMapping("/error/5xx")
    public String error5xx(){ return "forward:/frontend/5xx.html"; }

    @GetMapping("/price-list")
    public String priceList(){ return "forward:/frontend/price-list.html"; }
}
