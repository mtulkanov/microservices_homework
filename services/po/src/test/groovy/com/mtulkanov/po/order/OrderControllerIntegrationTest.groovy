package com.mtulkanov.po.order


import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.core.WireMockConfiguration
import com.github.tomakehurst.wiremock.junit.WireMockRule
import com.mtulkanov.eurekaserver.pc.catalog.ProductSpecification
import org.bson.types.ObjectId
import org.junit.Rule
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification

import static org.hamcrest.CoreMatchers.equalTo
import static org.hamcrest.CoreMatchers.is
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@AutoConfigureMockMvc
class OrderControllerIntegrationTest extends Specification {

    private static final String SPECIFICATION_ID = 'SPECIFICATION_ID'

    @Rule
    WireMockRule wireMockRule = new WireMockRule(
            WireMockConfiguration.options().port(8081)
    )

    @Autowired
    MockMvc mvc

    def 'should return order'() {
        given:
        def specification = new ProductSpecification(SPECIFICATION_ID, "cat", 100L)
        WireMock.stubFor(WireMock.get("/catalog/${SPECIFICATION_ID}")
                .willReturn(ResponseDefinitionBuilder.okForJson(specification)))

        expect:
        mvc.perform(put("/catalog/${SPECIFICATION_ID}/order"))
                .andExpect(status().isOk())
                .andExpect(jsonPath('$.specificationId', is(equalTo(SPECIFICATION_ID))))
    }
}
