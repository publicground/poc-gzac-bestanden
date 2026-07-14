import com.ritense.document.service.DocumentSearchService
import com.ritense.valtimo.common.service.DocumentReaderService
import com.ritense.valtimo.common.service.DocumentWriterService
import com.ritense.valtimo.waarderingmantelzorgersenvrijwilligers.service.WmvSearchService
import com.ritense.valueresolver.ValueResolverService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Disabled

class WmvSearchServiceTest {

    @Mock
    private lateinit var documentSearchService: DocumentSearchService

    @Mock
    private lateinit var documentReaderService: DocumentReaderService

    @Mock
    private lateinit var documentWriterService: DocumentWriterService

    @Mock
    private lateinit var valueResolverService: ValueResolverService

    @InjectMocks
    private lateinit var wmvSearchService: WmvSearchService

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.openMocks(this)
    }

    @Disabled
    @Test
    fun `valtimo doc path to jsonpath string`() {
        // Given
        val docPath = "doc:/foo/some/bar"

        // When
        val jsonPath = wmvSearchService.toJsonPath(docPath)

        // Then
        assertEquals("$.foo.some.bar", jsonPath)
    }
}
