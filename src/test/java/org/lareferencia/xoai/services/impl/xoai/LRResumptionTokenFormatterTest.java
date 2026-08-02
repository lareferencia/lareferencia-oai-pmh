package org.lareferencia.xoai.services.impl.xoai;

import com.lyncode.xoai.dataprovider.core.ResumptionToken;
import com.lyncode.xoai.dataprovider.exceptions.BadResumptionToken;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LRResumptionTokenFormatterTest {

    private final LRResumptionTokenFormatter formatter = new LRResumptionTokenFormatter();

    @Test
    void roundTripsEveryTokenComponent() throws BadResumptionToken {
        String encoded = "oai_dc/2026-08-01T00:00:00Z/2026-08-02T23:59:59Z/community:one/50";

        ResumptionToken parsed = formatter.parse(encoded);

        assertEquals(50, parsed.getOffset());
        assertEquals("oai_dc", parsed.getMetadataPrefix());
        assertEquals("community:one", parsed.getSet());
        assertEquals("oai_dc/2026-08-01T00:00:00.000Z/2026-08-02T23:59:59.000Z/community:one/50",
                formatter.format(parsed));
    }

    @Test
    void preservesEmptyOptionalComponents() throws BadResumptionToken {
        ResumptionToken parsed = formatter.parse("////0");

        assertEquals(0, parsed.getOffset());
        assertFalse(parsed.hasMetadataPrefix());
        assertFalse(parsed.hasFrom());
        assertFalse(parsed.hasUntil());
        assertFalse(parsed.hasSet());
        assertEquals("////0", formatter.format(parsed));
    }

    @Test
    void rejectsNegativeOffsets() {
        assertThrows(BadResumptionToken.class, () -> formatter.parse("////-1"));
    }

    @Test
    void rejectsNonNumericOffsets() {
        assertThrows(BadResumptionToken.class, () -> formatter.parse("////next"));
    }

    @Test
    void rejectsMalformedTokens() {
        assertThrows(BadResumptionToken.class, () -> formatter.parse("oai_dc/0"));
    }
}
