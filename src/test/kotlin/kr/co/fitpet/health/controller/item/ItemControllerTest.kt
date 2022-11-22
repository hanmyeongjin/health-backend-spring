package kr.co.fitpet.health.controller.item

import com.fasterxml.jackson.databind.ObjectMapper
import kr.co.fitpet.health.dto.item.ItemResponse
import kr.co.fitpet.health.dto.item.ItemSaveRequest
import kr.co.fitpet.health.exception.AuthorizationException
import kr.co.fitpet.health.service.item.ItemService
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.notNullValue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.context.WebApplicationContext


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
class ItemControllerTest {

    @Autowired
    private lateinit var wac: WebApplicationContext

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Autowired
    private lateinit var itemService: ItemService

    @Autowired
    private lateinit var mockMvc: MockMvc

    companion object {

        const val userToken = "GMH3VA5YY8FK1AFMF4J6FQ9PK2P67KBS0A9DR5VXAQAM5K2BC7DF0EHRXG12HE2M"

        var item = ItemSaveRequest(
            name = "testName1",
            key = "testKey1",
            cycleType = "MONTHLY",
            iconUrl = "http://test.test/",
            title = "title1",
            title2 = "title2",
            point = 100,
            maxPoint = 1000,
            isOpened = 1,
            position = 100,
            memo = "memo1"
        )
    }

    @BeforeEach
    fun setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build()
    }

    @Test
    fun `getItemList_정상`() {
        val uri = "/health/items/?page=1&page_size=1"
        mockMvc.perform(MockMvcRequestBuilders.get(uri).header("Authorization", userToken))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(
                MockMvcResultMatchers.content().string("{\"count\":0,\"next\":\"\",\"previous\":\"\",\"result\":[]}")
            )
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `getItemList_없는 사용자 토큰`() {
        val uri = "/health/items/?page=1&page_size=1"
        mockMvc.perform(MockMvcRequestBuilders.get(uri).header("Authorization", "1"))
            .andExpect(MockMvcResultMatchers.status().isUnauthorized)
            .andExpect(MockMvcResultMatchers.content().string(AuthorizationException.authorizeMessage))
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `getItem_정상`() {

        val saveItem = itemService.saveItem(userToken, item)

        val uri = "/health/items/${saveItem.id}"

        mockMvc.perform(MockMvcRequestBuilders.get(uri).header("Authorization", userToken))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.name", `is`(item.name)))
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `getItem_없는 아이템 아이디`() {

        val uri = "/health/items/1"

        mockMvc.perform(MockMvcRequestBuilders.get(uri).header("Authorization", userToken))
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `saveItem_정상`() {
        val content = objectMapper.writeValueAsString(item)
        val uri = "/health/items/"
        val postBody = mockMvc.perform(
            MockMvcRequestBuilders.post(uri)
                .header("Authorization", userToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
        )
            .andExpect(MockMvcResultMatchers.status().isCreated)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id", notNullValue()))
            .andExpect(jsonPath("$.name", `is`(item.name)))
            .andDo(MockMvcResultHandlers.print())
            .andReturn().response.contentAsString

        val saveItemResponse = objectMapper.readValue(postBody, ItemResponse::class.java)

        mockMvc.perform(MockMvcRequestBuilders.get("$uri/${saveItemResponse.id}").header("Authorization", userToken))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.name", `is`(item.name)))
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `modifyItem_정상`() {
        val saveItem = itemService.saveItem(userToken, item)

        val copyItem = item.copy(
            name = "copy item"
        )

        val content = objectMapper.writeValueAsString(copyItem)

        val uri = "/health/items/${saveItem.id}"

        mockMvc.perform(
            MockMvcRequestBuilders.patch(uri)
                .header("Authorization", userToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
        )
            .andExpect(MockMvcResultMatchers.status().isCreated)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.name", `is`(copyItem.name)))
            .andDo(MockMvcResultHandlers.print())

        mockMvc.perform(MockMvcRequestBuilders.get(uri).header("Authorization", userToken))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.name", `is`(copyItem.name)))
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `modifyItem_없는 아이템 아이디`() {

        val uri = "/health/items/1"

        val copyItem = item.copy(
            name = "copy item"
        )

        val content = objectMapper.writeValueAsString(copyItem)

        mockMvc.perform(
            MockMvcRequestBuilders.patch(uri)
                .header("Authorization", userToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andDo(MockMvcResultHandlers.print())
    }
}
