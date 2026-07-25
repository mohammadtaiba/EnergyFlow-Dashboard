package de.mohammad.energyflow.controller;

import de.mohammad.energyflow.entity.Site;
import de.mohammad.energyflow.repository.MeterRepository;
import de.mohammad.energyflow.repository.SiteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class MeterControllerTest
{
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MeterRepository meterRepository;

    @Autowired
    private SiteRepository siteRepository;

    @BeforeEach
    void cleanDatabase()
    {
        meterRepository.deleteAll();
        siteRepository.deleteAll();
    }

    @Test
    void createMeterReturnsCreatedMeter() throws Exception
    {
        Site site = saveSite("Berlin Office");

        mockMvc.perform(post("/api/meters")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                      "siteId": %d,
                      "name": "Main electricity meter",
                      "meterNumber": "STR-2026-001",
                      "energyType": "ELECTRICITY"
                    }
                    """.formatted(site.getId())))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.siteId").value(site.getId()))
            .andExpect(jsonPath("$.siteName").value("Berlin Office"))
            .andExpect(jsonPath("$.name").value("Main electricity meter"))
            .andExpect(jsonPath("$.meterNumber").value("STR-2026-001"))
            .andExpect(jsonPath("$.energyType").value("ELECTRICITY"));
    }

    @Test
    void getMetersBySiteIdReturnsOnlyMetersForThatSite() throws Exception
    {
        Site firstSite = saveSite("Berlin Office");
        Site secondSite = saveSite("Erfurt Production");

        createMeter(firstSite.getId(), "Main meter", "STR-2026-001", "ELECTRICITY");
        createMeter(secondSite.getId(), "Gas meter", "GAS-2026-001", "GAS");

        mockMvc.perform(get("/api/sites/{siteId}/meters", firstSite.getId()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].siteId").value(firstSite.getId()))
            .andExpect(jsonPath("$[0].name").value("Main meter"));
    }

    @Test
    void updateMeterChangesSiteAndFields() throws Exception
    {
        Site firstSite = saveSite("Berlin Office");
        Site secondSite = saveSite("Erfurt Production");
        Long meterId = createMeter(firstSite.getId(), "Main meter", "STR-2026-001", "ELECTRICITY");

        mockMvc.perform(put("/api/meters/{id}", meterId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                      "siteId": %d,
                      "name": "Updated gas meter",
                      "meterNumber": "GAS-2026-009",
                      "energyType": "GAS"
                    }
                    """.formatted(secondSite.getId())))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.siteId").value(secondSite.getId()))
            .andExpect(jsonPath("$.siteName").value("Erfurt Production"))
            .andExpect(jsonPath("$.name").value("Updated gas meter"))
            .andExpect(jsonPath("$.meterNumber").value("GAS-2026-009"))
            .andExpect(jsonPath("$.energyType").value("GAS"));
    }

    @Test
    void deleteMeterRemovesMeter() throws Exception
    {
        Site site = saveSite("Berlin Office");
        Long meterId = createMeter(site.getId(), "Main meter", "STR-2026-001", "ELECTRICITY");

        mockMvc.perform(delete("/api/meters/{id}", meterId))
            .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/meters/{id}", meterId))
            .andExpect(status().isNotFound());
    }

    @Test
    void createMeterWithMissingSiteReturnsNotFound() throws Exception
    {
        mockMvc.perform(post("/api/meters")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                      "siteId": 999,
                      "name": "Main meter",
                      "meterNumber": "STR-2026-001",
                      "energyType": "ELECTRICITY"
                    }
                    """))
            .andExpect(status().isNotFound());
    }

    private Site saveSite(String name)
    {
        Site site = new Site();

        site.setName(name);
        site.setType("OFFICE");
        site.setLocation("Berlin");

        return siteRepository.save(site);
    }

    private Long createMeter(Long siteId, String name, String meterNumber, String energyType) throws Exception
    {
        String location = mockMvc.perform(post("/api/meters")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                      "siteId": %d,
                      "name": "%s",
                      "meterNumber": "%s",
                      "energyType": "%s"
                    }
                    """.formatted(siteId, name, meterNumber, energyType)))
            .andExpect(status().isCreated())
            .andReturn()
            .getResponse()
            .getContentAsString();

        return Long.valueOf(location.replaceAll(".*\"id\":(\\d+).*", "$1"));
    }
}
