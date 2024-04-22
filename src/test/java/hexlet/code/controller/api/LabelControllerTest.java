package hexlet.code.controller.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.dto.labels.LabelCreateDTO;
import hexlet.code.mapper.LabelMapper;
import hexlet.code.model.Label;
import hexlet.code.repository.LabelRepository;
import hexlet.code.util.ModelGenerator;
import org.instancio.Instancio;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.Optional;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class LabelControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper om;

    @Autowired
    private LabelRepository labelRepository;

    @Autowired
    private ModelGenerator modelGenerator;

    private Label testLabel;

    @Value("/api/labels")
    private String url;

    @Autowired
    private LabelMapper labelMapper;

    @BeforeEach
    public void setUp() throws Exception {
        testLabel = Instancio.of(modelGenerator.getLabelModel()).create();
    }
    @AfterEach
    public void clear() {
        labelRepository.deleteAll();
    }

    @Test
    public void testIndex() throws Exception {
        var result = mockMvc.perform(get(url).with(jwt()))
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();
        assertThatJson(body).isArray();
    }

    @Test
    public  void testCreateLabel() throws Exception {
        Label testData = testLabel;
        LabelCreateDTO dto = labelMapper.mapToCreateDTO(testData);

        MockHttpServletRequestBuilder request = post(url).with(jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(dto));

        mockMvc.perform(request)
                .andExpect(status().isCreated());

        Optional<Label> label = labelRepository.findByName(testData.getName());

        assertThat(label).isNotNull();
        assertThat(label.get().getName()).isEqualTo(testData.getName());
    }

    @Test
    public  void testCreateLabelWithNotValidName() throws Exception {
        Label testData = testLabel;
        testData.setName("");
        LabelCreateDTO dto = labelMapper.mapToCreateDTO(testData);

        MockHttpServletRequestBuilder request = post(url).with(jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(dto));

        mockMvc.perform(request)
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testShowLabel() throws Exception {
        var testData = testLabel;
        labelRepository.save(testData);

        MockHttpServletRequestBuilder request = get(url + "/{id}", testData.getId()).with(jwt());


        MvcResult result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();
        assertThatJson(body).and(
                v -> v.node("name").isEqualTo(testData.getName())
        );
    }

    @Test
    public void testUpdateLabel() throws Exception {
        var testData = testLabel;
        labelRepository.save(testData);

        testData.setName("New name");

        LabelCreateDTO dto = labelMapper.mapToCreateDTO(testData);

        var request = put(url + "/{id}", testData.getId()).with(jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(dto));

        mockMvc.perform(request)
                .andExpect(status().isOk());

        var label = labelRepository.findByName(testData.getName()).get();
        assertThat(label.getName()).isEqualTo(testData.getName());
    }

    @Test
    public void testUpdateLabelWithNotValidName() throws Exception {
        var testData = testLabel;
        labelRepository.save(testData);

        testData.setName("1");

        LabelCreateDTO dto = labelMapper.mapToCreateDTO(testData);

        var request = put(url + "/{id}", testData.getId()).with(jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(dto));

        mockMvc.perform(request)
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testDestroy() throws Exception {
        var testData = testLabel;
        labelRepository.save(testData);

        var request = delete(url + "/{id}", testData.getId()).with(jwt());

        mockMvc.perform(request)
                .andExpect(status().isNoContent());

        assertThat(labelRepository.findByName(testData.getName())).isNotPresent();
    }
}
