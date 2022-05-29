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
    public void givenValidCharacterId_whenGetRequest_thenShouldResponseOkWithTheCharacter() throws Exception {
        ResultActions result = mockMvc.perform(get("/characters/" + testCharacter.getId()));

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.character.name", is(testCharacter.getName())));
        result.andExpect(jsonPath("$.character.imageUrl", is(testCharacter.getImageUrl())));
        result.andExpect(jsonPath("$.character.age", is(testCharacter.getAge())));
        result.andExpect(jsonPath("$.character.weight", is(testCharacter.getWeight())));
        result.andExpect(jsonPath("$.character.story", is(testCharacter.getStory())));
    }

    @Test
    public void givenInvalidCharacterId_whenGetRequest_thenShouldResponseNotFound() throws Exception {
        ResultActions result = mockMvc.perform(get("/characters/" + (testCharacter.getId() + 1)));

        result.andExpect(status().isNotFound());
    }

    @Test
    public void givenAValidPersistedCharacter_whenGetRequest_thenShouldResponseOkWithAListWithOneCharacter() throws Exception {
        ResultActions result = mockMvc.perform(get("/characters"));

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.characters", hasSize(1)));
        result.andExpect(jsonPath("$.characters[0].name", is(testCharacter.getName())));
        result.andExpect(jsonPath("$.characters[0].imageUrl", is(testCharacter.getImageUrl())));
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

        int charactersPersisted = (int) characterRepo.count();
        assertEquals(charactersPersisted, 2);

        result.andExpect(status().isCreated());
        result.andExpect(jsonPath("$.character.name", is(newCharacter.getName())));
        result.andExpect(jsonPath("$.character.imageUrl", is(newCharacter.getImageUrl())));
        result.andExpect(jsonPath("$.character.story", is(newCharacter.getStory())));
    }

    @Test
    public void givenInvalidCharacter_whenPostRequest_thenShouldResponseBadRequest() throws Exception {
        Character newCharacter = Character
                .builder()
                .imageUrl("https://www.image.com/images?newimage")
                .story("a long text")
                .build();
        String newJsonCharacter = "{\"imageUrl\":\"" + newCharacter.getImageUrl() + "\"," +
                "\"story\":\"" + newCharacter.getStory() + "\"}";

        ResultActions result = mockMvc.perform(post("/characters")
                .content(newJsonCharacter)
                .contentType(APPLICATION_JSON_VALUE));

        int charactersPersisted = (int) characterRepo.count();
        assertEquals(charactersPersisted, 1);

        result.andExpect(status().isBadRequest());
    }

    // -----------------------------------------------------
    //              Test for DELETE CHARACTER
    // -----------------------------------------------------
    @Test
    public void givenValidCharacterId_whenDeleteRequest_thenShouldResponseOk() throws Exception {
        ResultActions result = mockMvc.perform(delete("/characters/" + testCharacter.getId()));

        int charactersPersisted = (int) characterRepo.count();
        assertEquals(charactersPersisted, 0);

        result.andExpect(status().isOk());
    }

    @Test
    public void givenInvalidCharacterId_whenDeleteRequest_thenShouldResponseNotFound() throws Exception {
        long invalidCharacterId = testCharacter.getId() + 1;
        ResultActions result = mockMvc.perform(delete("/characters/" + invalidCharacterId));

        int charactersPersisted = (int) characterRepo.count();
        assertEquals(charactersPersisted, 1);

        result.andExpect(status().isNotFound());
        result.andExpect(jsonPath("$.message", is("Character with id " + invalidCharacterId + " does no exist")));
    }

    // -----------------------------------------------------
    //              Test for UPDATE CHARACTER
    // -----------------------------------------------------
    @Test
    public void givenValidCharacterData_whenPatchRequest_thenShouldResponseOkAndUpdatedCharacter() throws Exception {
        String newName = "test character 2";
        int newAge = 60;
        String newJsonData = "{\"name\":\"" + newName + "\"," +
                "\"age\":\"" + newAge + "\"}";

        ResultActions result = mockMvc.perform(patch("/characters/" + testCharacter.getId())
                .content(newJsonData)
                .contentType(APPLICATION_JSON_VALUE));

        int charactersPersisted = (int) characterRepo.count();
        assertEquals(charactersPersisted, 1);

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.character.name", is(newName)));
        result.andExpect(jsonPath("$.character.imageUrl", is(testCharacter.getImageUrl())));
        result.andExpect(jsonPath("$.character.age", is(newAge)));
        result.andExpect(jsonPath("$.character.weight", is(testCharacter.getWeight())));
        result.andExpect(jsonPath("$.character.story", is(testCharacter.getStory())));
    }

    @Test
    public void givenInvalidCharacterId_whenPatchRequest_thenShouldResponseNotFound() throws Exception {
        String newName = "test character 2";
        int newAge = 60;
        String newJsonData = "{\"name\":\"" + newName + "\"," +
                "\"age\":\"" + newAge + "\"}";

        ResultActions result = mockMvc.perform(patch("/characters/" + (testCharacter.getId() + 1))
                .content(newJsonData)
                .contentType(APPLICATION_JSON_VALUE));

        result.andExpect(status().isNotFound());
    }

    @Test
    public void givenEmptyCharacterData_whenPatchRequest_thenShouldResponseBadRequest() throws Exception {
        String newJsonData = "{}";

        ResultActions result = mockMvc.perform(patch("/characters/" + testCharacter.getId())
                .content(newJsonData)
                .contentType(APPLICATION_JSON_VALUE));

        result.andExpect(status().isBadRequest());
        result.andExpect(jsonPath("$.message", is("You must send a property to update")));
    }
}
