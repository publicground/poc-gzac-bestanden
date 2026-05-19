import com.ritense.valtimo.TestHelper
import com.ritense.valtimo.haalcentraalkvk.client.HcKvkHandelsregisterClient
import com.ritense.valtimo.haalcentraalkvk.client.ClientConfig
import com.ritense.valtimo.haalcentraalkvk.client.SamlTokenWebClient
import com.ritense.valtimo.haalcentraalkvk.model.HcMaatschappelijkeActiviteiten
import com.ritense.valtimo.haalcentraalkvk.model.VestigingResponse
import com.ritense.valtimo.haalcentraalkvk.service.HcKvkHandelsregisterService
import mu.KotlinLogging
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.InjectMocks
import org.mockito.MockitoAnnotations
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Disabled
import org.mockito.Mock
import org.mockito.kotlin.whenever
import java.net.URI

class HcKvkHandelsregisterServiceTest {

    lateinit var testHelper: TestHelper

    private val logger = KotlinLogging.logger { }
    @Mock
    private lateinit var kvkHandelsregisterClient: HcKvkHandelsregisterClient

    @Mock
    private lateinit var samlTokenWebClient: SamlTokenWebClient

    @InjectMocks
    private lateinit var hcKvkHandelsregisterService: HcKvkHandelsregisterService

    @Mock
    private lateinit var clientConfig: ClientConfig

    @BeforeEach
    fun setUp() {
        testHelper = TestHelper
        MockitoAnnotations.openMocks(this)
    }

    @Disabled
    @Test
    fun `retrieve data from kvk and return vestigingen`() {
        // Given
        val kvkNumber = "69599084"
        val samlToken = "123"
        val vestingNummer1 = "000038509504"
        val vestingNummer2 = "000038509520"
        var uri = URI("")

        // When
        whenever(samlTokenWebClient.getToken(clientConfig)).thenReturn(samlToken)
        whenever(kvkHandelsregisterClient.get<HcMaatschappelijkeActiviteiten>(uri, clientConfig, samlToken)).thenReturn(
            testHelper.deserialiseJsonAsObject<HcMaatschappelijkeActiviteiten>("classpath:kvk/dtos/$kvkNumber.json")
        )

        uri = URI(vestingNummer1)
        whenever(
            kvkHandelsregisterClient.get<VestigingResponse>(uri, clientConfig, samlToken)
        ).thenAnswer {
            testHelper.deserialiseJsonAsObject<VestigingResponse>("classpath:kvk/dtos/vestiging_${kvkNumber}_$vestingNummer1.json")
        }

        uri = URI(vestingNummer1)
        whenever(
            kvkHandelsregisterClient.get<VestigingResponse>(uri, clientConfig, samlToken)
        ).thenAnswer {
            testHelper.deserialiseJsonAsObject<VestigingResponse>("classpath:kvk/dtos/vestiging_${kvkNumber}_$vestingNummer2.json")
        }

        val result = hcKvkHandelsregisterService.zoekOpKvkNummer(clientConfig, kvkNumber, 10)

        // then
        assertEquals(kvkNumber, result?.kvkNummer)
    }
}
