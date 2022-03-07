// package edu.ucsb.cs156.example.services;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
// import org.springframework.http.MediaType;
// import org.springframework.test.web.client.MockRestServiceServer;
// import edu.ucsb.cs156.example.entities.UCSBSubject;

// import com.fasterxml.jackson.core.JsonProcessingException;
// import com.fasterxml.jackson.core.type.TypeReference;
// import com.fasterxml.jackson.databind.JsonMappingException;
// import com.fasterxml.jackson.databind.ObjectMapper;
// import java.util.ArrayList;
// import java.util.Arrays;
// import java.util.List;
// import org.junit.jupiter.api.Test;
// import static org.junit.jupiter.api.Assertions.assertEquals;
// import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
// import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
// import static org.springframework.test.web.client.match.MockRestRequestMatchers.header;

// import org.springframework.beans.factory.annotation.Value;

// @RestClientTest(UCSBSubjectsService.class)
// public class UCSBSubjectsServiceTests {

//   @Autowired
//   private MockRestServiceServer mockRestServiceServer;

//   @Autowired
//   private UCSBSubjectsService ucsbSubjectService;

//   @Value("${app.ucsb.api.consumer_key}")
//   private String apiKey;

//   ObjectMapper mapper = new ObjectMapper();

//   @Test
//   public void test_getJSON() {

//     String expectedURL = UCSBSubjectsService.ENDPOINT;
//     String fakeJsonResult = "{ \"fake\" : \"result\" }";

//     this.mockRestServiceServer.expect(requestTo(expectedURL))
//         .andExpect(header("Accept", MediaType.APPLICATION_JSON.toString()))
//         .andExpect(header("Content-Type", MediaType.APPLICATION_JSON.toString()))
//         .andExpect(header("ucsb-api-key", this.apiKey))
//         .andExpect(header("ucsb-api-version", "1.0"))
//         .andRespond(withSuccess(fakeJsonResult, MediaType.APPLICATION_JSON));

//     assertEquals(fakeJsonResult, ucsbSubjectService.getJSON());
//   }

//   @Test
//   public void test_get() throws JsonMappingException, JsonProcessingException {

//     String expectedURL = UCSBSubjectsService.ENDPOINT;
//     String JsonResult = "{ \"fake\" : \"result\" }";

//     this.mockRestServiceServer.expect(requestTo(expectedURL))
//         .andExpect(header("Accept", MediaType.APPLICATION_JSON.toString()))
//         .andExpect(header("Content-Type", MediaType.APPLICATION_JSON.toString()))
//         .andExpect(header("ucsb-api-key", this.apiKey))
//         .andExpect(header("ucsb-api-version", "1.0"))
//         .andRespond(withSuccess(JsonResult, MediaType.APPLICATION_JSON));

//     List<UCSBSubject> fakeResult = mapper.readValue(JsonResult, new TypeReference<List<UCSBSubject>>() {
//     });

//     // List<UCSBSubject> fakeResult = new ArrayList<>();
//     // try {
//     // UCSBSubject[] subjects = mapper.readValue(JsonResult, UCSBSubject[].class);
//     // fakeResult = new ArrayList(Arrays.asList(subjects));

//     // } catch (Exception e) {
//     // e.printStackTrace();
//     // }

//     assertEquals(fakeResult, ucsbSubjectService.get());
//   }
// }

package edu.ucsb.cs156.example.services;

import edu.ucsb.cs156.example.entities.UCSBSubject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.header;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.test.web.client.MockRestServiceServer;

import org.mockito.MockitoAnnotations;
import org.mockito.Mock;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

@RestClientTest(UCSBSubjectsService.class)
class UCSBSubjectsServiceTests {

  @Autowired
  private MockRestServiceServer mockRestServiceServer;

  @Autowired
  private UCSBSubjectsService ucsbSubjectsService;

  @Value("${app.ucsb.api.consumer_key}")
  private String apiKey;

  private static final String subjectCode = "CS156";
  private static final String subjectTranslation = "Computer Science";
  private static final String deptCode = "CPMSC";
  private static final String collegeCode = "UCSB";
  private static final String relatedDeptCode = "ENGR";
  private static final Boolean inactive = false;

  @Test
  void test_get() throws Exception {

    String expectedURL = ucsbSubjectsService.ENDPOINT;

    String fakeJSON = String.format("""
        [
        {
        \"subjectCode\": \"%s\",
        \"subjectTranslation\":\"%s\",
        \"deptCode\": \"%s\",
        \"collegeCode\": \"%s\",
        \"relatedDeptCode\": \"%s\",
        \"inactive\": \"%s\"
        }
        ]
        """, subjectCode, subjectTranslation, deptCode, collegeCode, relatedDeptCode,
        String.valueOf(inactive));

    UCSBSubject fakeSubject = UCSBSubject.builder()
        .subjectCode(subjectCode)
        .subjectTranslation(subjectTranslation)
        .deptCode(deptCode)
        .collegeCode(collegeCode)
        .relatedDeptCode(relatedDeptCode)
        .inactive(inactive)
        .build();

    this.mockRestServiceServer.expect(requestTo(expectedURL))
        .andExpect(header("Accept", MediaType.APPLICATION_JSON.toString()))
        .andExpect(header("Content-Type", MediaType.APPLICATION_JSON.toString()))
        .andExpect(header("ucsb-api-key", apiKey))
        .andExpect(header("ucsb-api-version", "1.0"))
        .andRespond(withSuccess(fakeJSON, MediaType.APPLICATION_JSON));
    List<UCSBSubject> actualList = new ArrayList<>();
    actualList = ucsbSubjectsService.get();
    List<UCSBSubject> fakeList = new ArrayList<>();
    fakeList.addAll(Arrays.asList(fakeSubject));
    assertEquals(fakeList, actualList);
    assertEquals(fakeList.size(), actualList.size());
  }

}