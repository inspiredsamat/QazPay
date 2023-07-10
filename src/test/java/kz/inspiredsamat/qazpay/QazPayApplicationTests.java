package kz.inspiredsamat.qazpay;

import com.fasterxml.jackson.databind.ObjectMapper;
import kz.inspiredsamat.qazpay.model.Account;
import kz.inspiredsamat.qazpay.model.Role;
import kz.inspiredsamat.qazpay.model.Transfer;
import kz.inspiredsamat.qazpay.model.User;
import kz.inspiredsamat.qazpay.model.dto.AccountDTO;
import kz.inspiredsamat.qazpay.model.dto.UserDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class QazPayApplicationTests {

	private final ObjectMapper mapper = new ObjectMapper();

	@Autowired
	private WebApplicationContext context;

	private MockMvc mockMvc;

	@BeforeEach
	public void setUp() {
		mockMvc = MockMvcBuilders.webAppContextSetup(context)
				.build();
	}

	@Test
	public void testCreateUser() throws Exception {
		User newUserBody = new User();
		newUserBody.setFirstName("Ivan");
		newUserBody.setLastName("Ivan");
		newUserBody.setEmail("ivan@gmail.com");
		newUserBody.setPassword("ivanivan");
		newUserBody.setUsername("inspiredivan");
		newUserBody.setRole(Role.USER);

		MvcResult registerResult = mockMvc.perform(post("/qazpay/api/users/register")
						.contentType(MediaType.APPLICATION_JSON)
						.content(mapper.writeValueAsString(newUserBody)))
				.andExpect(status().isCreated()).andReturn()
				;

		UserDTO createdUser = mapper.readValue(registerResult.getResponse().getContentAsString(), UserDTO.class);

		MvcResult getResult = mockMvc.perform(get("/qazpay/api/users/" + createdUser.getId())).andReturn();

		UserDTO userFromDatabase = mapper.readValue(getResult.getResponse().getContentAsString(), UserDTO.class);

		assertEquals(newUserBody.getUsername(), userFromDatabase.getUsername());
	}

	/*
		Account with id 100 does not exist in a database
	 */

	@Test
	public void testMakeUnsuccessfulTransfer() throws Exception {
		User newUserBody = new User();
		newUserBody.setFirstName("Pavel");
		newUserBody.setLastName("Pavel");
		newUserBody.setEmail("pavel@gmail.com");
		newUserBody.setPassword("pavelpavel");
		newUserBody.setUsername("inspiredpavel");
		newUserBody.setRole(Role.USER);

		mockMvc.perform(post("/qazpay/api/users/register")
						.contentType(MediaType.APPLICATION_JSON)
						.content(mapper.writeValueAsString(newUserBody)))
				.andExpect(status().isCreated()).andReturn();

		UsernamePasswordAuthenticationToken authenticationToken =
				new UsernamePasswordAuthenticationToken(newUserBody.getUsername(), null);
		SecurityContextHolder.getContext().setAuthentication(authenticationToken);

		Transfer newTransferBody = new Transfer();
		newTransferBody.setFromAccountId(100L);
		newTransferBody.setToAccountId(200L);
		newTransferBody.setTransferAmount(10000L);

		mockMvc.perform(post("/qazpay/api/transfer")
						.contentType(MediaType.APPLICATION_JSON)
						.content(mapper.writeValueAsString(newTransferBody)))
				.andExpect(status().isNotFound());
	}

	@Test
	public void testMakeTransfer() throws Exception {
		User newUserBody1 = new User();
		newUserBody1.setFirstName("Natasha");
		newUserBody1.setLastName("Natasha");
		newUserBody1.setEmail("natasha@gmail.com");
		newUserBody1.setPassword("natasha");
		newUserBody1.setUsername("inspirednatasha");
		newUserBody1.setRole(Role.USER);

		User newUserBody2 = new User();
		newUserBody2.setFirstName("Vladimir");
		newUserBody2.setLastName("Vladimir");
		newUserBody2.setEmail("vladimir@gmail.com");
		newUserBody2.setPassword("vladimir");
		newUserBody2.setUsername("inspiredvladimir");
		newUserBody2.setRole(Role.USER);

		mockMvc.perform(post("/qazpay/api/users/register")
						.contentType(MediaType.APPLICATION_JSON)
						.content(mapper.writeValueAsString(newUserBody1)))
				.andExpect(status().isCreated());

		mockMvc.perform(post("/qazpay/api/users/register")
						.contentType(MediaType.APPLICATION_JSON)
						.content(mapper.writeValueAsString(newUserBody2)))
				.andExpect(status().isCreated());

		UsernamePasswordAuthenticationToken authenticationToken1 =
				new UsernamePasswordAuthenticationToken(newUserBody1.getUsername(), null);
		SecurityContextHolder.getContext().setAuthentication(authenticationToken1);

		Account newAccountBody1 = new Account();
		newAccountBody1.setCardNumber("4654611864543");

		MvcResult accountCreateResult1 = mockMvc.perform(post("/qazpay/api/accounts")
						.contentType(MediaType.APPLICATION_JSON)
						.content(mapper.writeValueAsString(newAccountBody1)))
				.andReturn();

		UsernamePasswordAuthenticationToken authenticationToken2 =
				new UsernamePasswordAuthenticationToken(newUserBody2.getUsername(), null);
		SecurityContextHolder.getContext().setAuthentication(authenticationToken2);

		Account newAccountBody2 = new Account();
		newAccountBody2.setCardNumber("2342415313544");
		newAccountBody2.setBalance(50000L);

		MvcResult accountCreateResult2 = mockMvc.perform(post("/qazpay/api/accounts")
						.contentType(MediaType.APPLICATION_JSON)
						.content(mapper.writeValueAsString(newAccountBody2)))
				.andReturn();

		AccountDTO createdAccount1 = mapper.readValue(accountCreateResult1.getResponse().getContentAsString(), AccountDTO.class);
		AccountDTO createdAccount2 = mapper.readValue(accountCreateResult2.getResponse().getContentAsString(), AccountDTO.class);

		Transfer newTransferBody = new Transfer();
		newTransferBody.setFromAccountId(createdAccount2.getId());
		newTransferBody.setToAccountId(createdAccount1.getId());
		newTransferBody.setTransferAmount(10000L);

		mockMvc.perform(post("/qazpay/api/transfer")
						.contentType(MediaType.APPLICATION_JSON)
						.content(mapper.writeValueAsString(newTransferBody)));

		MvcResult accountGetResult1 = mockMvc.perform(get("/qazpay/api/accounts/" + createdAccount1.getId()))
				.andReturn();

		MvcResult accountGetResult2 = mockMvc.perform(get("/qazpay/api/accounts/" + createdAccount2.getId()))
				.andReturn();

		AccountDTO accountFromDatabase1 = mapper.readValue(accountGetResult1.getResponse().getContentAsString(), AccountDTO.class);
		AccountDTO accountFromDatabase2 = mapper.readValue(accountGetResult2.getResponse().getContentAsString(), AccountDTO.class);

		assertEquals(10000L, accountFromDatabase1.getBalance());
		assertEquals(40000L, accountFromDatabase2.getBalance());
	}

	@Test
	public void testCreateAccount() throws Exception {
		User newUserBody = new User();
		newUserBody.setFirstName("Alexei");
		newUserBody.setLastName("Alexei");
		newUserBody.setEmail("alexei@gmail.com");
		newUserBody.setPassword("alexei");
		newUserBody.setUsername("inspiredalexei");
		newUserBody.setRole(Role.USER);

		MvcResult userRegisterResult = mockMvc.perform(post("/qazpay/api/users/register")
						.contentType(MediaType.APPLICATION_JSON)
						.content(mapper.writeValueAsString(newUserBody)))
				.andExpect(status().isCreated()).andReturn()
				;

		UserDTO createdUser = mapper.readValue(userRegisterResult.getResponse().getContentAsString(), UserDTO.class);

		UsernamePasswordAuthenticationToken authenticationToken =
				new UsernamePasswordAuthenticationToken(newUserBody.getUsername(), null);
		SecurityContextHolder.getContext().setAuthentication(authenticationToken);

		Account newAccountBody = new Account();
		newAccountBody.setCardNumber("123455676888");

		MvcResult cardCreateResult = mockMvc.perform(post("/qazpay/api/accounts")
						.contentType(MediaType.APPLICATION_JSON)
						.content(mapper.writeValueAsString(newAccountBody)))
				.andReturn();

		AccountDTO createdAccount = mapper.readValue(cardCreateResult.getResponse().getContentAsString(), AccountDTO.class);

		assertEquals(0L, createdAccount.getBalance());
		assertEquals(createdUser.getId(), createdAccount.getAccountOwnerId());
	}
}
