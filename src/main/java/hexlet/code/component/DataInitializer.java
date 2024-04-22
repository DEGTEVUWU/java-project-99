package hexlet.code.component;

import hexlet.code.dto.users.UserCreateDTO;
import hexlet.code.mapper.UserMapper;
import hexlet.code.model.Label;
import hexlet.code.model.TaskStatus;
import hexlet.code.model.User;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.repository.UserRepository;
import hexlet.code.service.CustomUserDetailsService;
import hexlet.code.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@AllArgsConstructor
public class DataInitializer implements ApplicationRunner {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private TaskStatusRepository taskStatusRepository;
    @Autowired
    private LabelRepository labelRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public void run(ApplicationArguments args) {
        var email = "hexlet@example.com";
        var userData = new User();
        userData.setEmail(email);
        userData.setPasswordDigest(passwordEncoder.encode("qwerty"));
        userRepository.save(userData);

        taskStatusesInitializer();
        labelsInitializer();
    }

    public final void taskStatusesInitializer() { //метод для первичной инициализации 5 статусов для задач
        Map<String, String> firstStructure = new HashMap<>();

        firstStructure.put("Draft", "draft");
        firstStructure.put("ToReview", "to_review");
        firstStructure.put("ToBeFixed", "to_be_fixed");
        firstStructure.put("ToPublish", "to_publish");
        firstStructure.put("Published", "published");

        firstStructure.entrySet().stream()
                .map(entry -> {
                    TaskStatus taskStatus = new TaskStatus();
                    taskStatus.setName(entry.getKey());
                    taskStatus.setSlug(entry.getValue());
                    return taskStatus;
                })
                .forEach(taskStatusRepository::save);

    }
    public final void labelsInitializer() {//метод для первичной инициализации 2 лейблов для задач
        Label label1 = new Label();
        Label label2 = new Label();

        label1.setName("feature");
        label2.setName("bug");

        labelRepository.save(label1);
        labelRepository.save(label2);

    }
}
