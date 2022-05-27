package alkemy.challenge.backend;

import alkemy.challenge.backend.config.ContainersEnvironment;
import alkemy.challenge.backend.controller.CharacterController;
import alkemy.challenge.backend.entity.Character;
import alkemy.challenge.backend.repository.CharacterRepository;
import alkemy.challenge.backend.service.CharacterService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = BackendApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CharacterControllerIntegrationTest extends ContainersEnvironment {

    @Autowired
    CharacterRepository characterRepo;

    @Autowired
    CharacterService characterService;

    @Autowired
    CharacterController characterController;

    @Autowired
    ModelMapper mapper;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    private Character testCharacter;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        testCharacter = Character
                .builder()
                .name("test character")
                .imageUrl("https://www.image.com/images?someimage")
                .age(50)
                .weight(50)
                .story("a long text")
                .build();
        testCharacter = characterRepo.save(testCharacter);
    }

    @AfterEach
    public void tearDown() {
        characterRepo.deleteAll();
    }

    // -----------------------------------------------------
    //              Test for GET CHARACTER
    // -----------------------------------------------------
    @Test
    public void givenAValidPersistedCharacter_whenGetRequest_thenShouldResponseOkWithAListWithOneCharacter() throws Exception {
        ResultActions result = mockMvc.perform(get("/characters"));

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.characters", hasSize(1)));
        result.andExpect(jsonPath("$.characters[0].name", is(testCharacter.getName())));
        result.andExpect(jsonPath("$.characters[0].imageUrl", is(testCharacter.getImageUrl())));
        result.andExpect(jsonPath("$.characters[0].age", is(testCharacter.getAge())));
        result.andExpect(jsonPath("$.characters[0].weight", is(testCharacter.getWeight())));
        result.andExpect(jsonPath("$.characters[0].story", is(testCharacter.getStory())));
    }

    // -----------------------------------------------------
    //              Test for POST CHARACTER
    // -----------------------------------------------------
    @Test
    public void givenValidCharacter_whenPostRequest_thenShouldResponseCreatedAndReturnData() throws Exception {
        Character newCharacter = Character
                .builder()
                .name("new character")
                .imageUrl("https://www.image.com/images?newimage")
                .story("a long text")
                .build();
        String newJsonCharacter = "{\"name\":\"" + newCharacter.getName() + "\"," +
                                   "\"imageUrl\":\"" + newCharacter.getImageUrl() + "\"," +
                                   "\"story\":\"" + newCharacter.getStory() + "\"}";

        ResultActions result = mockMvc.perform(post("/characters")
                .content(newJsonCharacter)
                .contentType(APPLICATION_JSON_VALUE));

        int charactersPersisted = (int)characterRepo.count();
        assertEquals(charactersPersisted, 2);

        result.andExpect(status().isCreated());
        result.andExpect(jsonPath("$.character.name", is(newCharacter.getName())));
        result.andExpect(jsonPath("$.character.imageUrl", is(newCharacter.getImageUrl())));
        result.andExpect(jsonPath("$.character.story", is(newCharacter.getStory())));
    }
}
