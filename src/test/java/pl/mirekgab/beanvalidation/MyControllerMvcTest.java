package pl.mirekgab.beanvalidation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

/**
 *
 * @author mirek
 */
//test mvc
@RunWith(SpringRunner.class)
@WebMvcTest(controllers = MyController.class)
@TestPropertySource(locations = "classpath:application-test-mvc.properties")
@TestInstance(Lifecycle.PER_CLASS)
public class MyControllerMvcTest {

    @MockBean
    private static ClientRepository clientRepository;

    @Autowired
    MockMvc mockMvc;

    private LocalDate birthDate;
    private ObjectMapper objectMapper;

    @BeforeAll
    public void setUp() throws ParseException {
        this.birthDate = LocalDate.parse("2000-11-20");
        this.objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
    }

    @Test
    public void addValidClient() throws Exception {
        Client client = new Client();
        client.setId(1L);
        client.setName("Adam");
        client.setCity("Andrychow");
        client.setPostalCode("16-300");
        client.setEmail("adam@andrychow.pl");
        client.setBirthDate(birthDate);
        client.setPositiveNumber(1);

        Mockito.when(clientRepository.save(Mockito.any(Client.class))).thenReturn(client);

        String clientString = objectMapper.writeValueAsString(client);
        mockMvc.perform(MockMvcRequestBuilders.post("/add")
                .content(clientString)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(clientString));
    }

    @Test
    public void nameEmptyIsInvalid() throws Exception {
        Client client = new Client();
        client.setId(1L);
        client.setName("");
        client.setCity("Andrychów");
        client.setPostalCode("16-300");
        client.setEmail("adam@andrychow.pl");
        client.setBirthDate(birthDate);
        client.setPositiveNumber(1);

        Mockito.when(clientRepository.save(client)).thenReturn(client);

        String clientString = objectMapper.writeValueAsString(client);
        mockMvc.perform(MockMvcRequestBuilders.post("/add")
                .content(clientString)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("{\"name\":\"name is mandatory\"}"));
    }

    @Test
    public void cityEmptyIsInvalid() throws Exception {
        Client client = new Client();
        client.setId(1L);
        client.setName("Adam");
        client.setCity("");
        client.setPostalCode("16-300");
        client.setEmail("adam@andrychow.pl");
        client.setBirthDate(birthDate);
        client.setPositiveNumber(1);

        Mockito.when(clientRepository.save(Mockito.any(Client.class))).thenReturn(client);

        String sc = objectMapper.writeValueAsString(client);
        mockMvc.perform(MockMvcRequestBuilders.post("/add")
                .content(sc)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("{\"city\":\"city is mandatory\"}"));
    }

    @Test
    public void postalCodeWrongFormatIsInvalid() throws Exception {
        Client client = new Client();
        client.setId(1L);
        client.setName("Adam");
        client.setCity("Andrychow");
        client.setPostalCode("161-300");
        client.setEmail("adam@andrychow.pl");
        client.setBirthDate(birthDate);
        client.setPositiveNumber(1);

        Mockito.when(clientRepository.save(Mockito.any(Client.class))).thenReturn(client);

        String sc = objectMapper.writeValueAsString(client);
        mockMvc.perform(MockMvcRequestBuilders.post("/add")
                .content(sc)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("{\"postalCode\":\"postal code must match format XX-XXX\"}"));
    }

    @Test
    public void emailEmptyIsInvalid() throws Exception {
        Client client = new Client();
        client.setId(1L);
        client.setName("Adam");
        client.setCity("Andrychow");
        client.setPostalCode("16-300");
        client.setEmail("");
        client.setBirthDate(birthDate);
        client.setPositiveNumber(1);

        Mockito.when(clientRepository.save(Mockito.any(Client.class))).thenReturn(client);

        String stringClient = objectMapper.writeValueAsString(client);

        mockMvc.perform(MockMvcRequestBuilders.post("/add")
                .content(stringClient)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("{\"email\":\"must not be blank\"}"));

    }

    @Test
    public void emailWrongFormatIsInvalid() throws Exception {
        Client client = new Client();
        client.setId(1L);
        client.setName("Adam");
        client.setCity("Andrychow");
        client.setPostalCode("16-300");
        client.setEmail("adam");
        client.setBirthDate(birthDate);
        client.setPositiveNumber(1);

        Mockito.when(clientRepository.save(Mockito.any(Client.class))).thenReturn(client);
        String stringClient = objectMapper.writeValueAsString(client);
        mockMvc.perform(MockMvcRequestBuilders.post("/add")
                .content(stringClient)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("{\"email\":\"must be a well-formed email address\"}"));
    }
    
    @Test
    public void birthDateInPastIsValid() throws Exception {
        Client client = new Client();
        client.setId(1L);
        client.setName("Adam");
        client.setCity("Andrychow");
        client.setPostalCode("16-300");
        client.setEmail("adam@andrychow.pl");
        client.setBirthDate(LocalDate.now().minusMonths(1));
        client.setPositiveNumber(1);

        Mockito.when(clientRepository.save(Mockito.any(Client.class))).thenReturn(client);
        String stringClient = objectMapper.writeValueAsString(client);
        mockMvc.perform(MockMvcRequestBuilders.post("/add")
                .content(stringClient)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }    

    @Test
    public void birthDatePresentIsValid() throws Exception {
        Client client = new Client();
        client.setId(1L);
        client.setName("Adam");
        client.setCity("Andrychow");
        client.setPostalCode("16-300");
        client.setEmail("adam@andrychow.pl");
        client.setBirthDate(LocalDate.now());
        client.setPositiveNumber(1);

        Mockito.when(clientRepository.save(Mockito.any(Client.class))).thenReturn(client);
        String stringClient = objectMapper.writeValueAsString(client);
        mockMvc.perform(MockMvcRequestBuilders.post("/add")
                .content(stringClient)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void birthDateInFutureIsInvalid() throws Exception {
        Client client = new Client();
        client.setId(1L);
        client.setName("Adam");
        client.setCity("Andrychow");
        client.setPostalCode("16-300");
        client.setEmail("adam@andrychow.pl");
        client.setBirthDate(LocalDate.now().plusDays(1));
        client.setPositiveNumber(1);

        Mockito.when(clientRepository.save(Mockito.any(Client.class))).thenReturn(client);
        String stringClient = objectMapper.writeValueAsString(client);
        mockMvc.perform(MockMvcRequestBuilders.post("/add")
                .content(stringClient)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("{\"birthDate\":\"birth date must be past or present\"}"));
    }

    @Test
    public void positiveNumberNegativeNumberIsInvalid() throws Exception {
        Client client = new Client();
        client.setId(1L);
        client.setName("Adam");
        client.setCity("Andrychow");
        client.setPostalCode("16-300");
        client.setEmail("adam@andrychow.pl");
        client.setBirthDate(birthDate);
        client.setPositiveNumber(-1);

        Mockito.when(clientRepository.save(Mockito.any(Client.class))).thenReturn(client);
        String stringClient = objectMapper.writeValueAsString(client);
        mockMvc.perform(MockMvcRequestBuilders.post("/add")
                .content(stringClient)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("{\"positiveNumber\":\"must be greater than 0\"}"));
    }

    @Test
    public void positiveNumberZeroIsInvalid() throws Exception {
        Client client = new Client();
        client.setId(1L);
        client.setName("Adam");
        client.setCity("Andrychow");
        client.setPostalCode("16-300");
        client.setEmail("adam@andrychow.pl");
        client.setBirthDate(LocalDate.now().minusDays(1));
        client.setPositiveNumber(0);

        Mockito.when(clientRepository.save(Mockito.any(Client.class))).thenReturn(client);
        String stringClient = objectMapper.writeValueAsString(client);
        mockMvc.perform(MockMvcRequestBuilders.post("/add")
                .content(stringClient)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("{\"positiveNumber\":\"must be greater than 0\"}"));
    }
}
