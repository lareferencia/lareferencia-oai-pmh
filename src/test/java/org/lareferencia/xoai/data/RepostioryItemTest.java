package org.lareferencia.xoai.data;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RepostioryItemTest {

    @Test
    void preservesTheHandleAsThePublicOaiIdentifier() {
        String handle = "20.500.12345/678";

        assertEquals(handle, RepostioryItem.buildIdentifier(handle));
    }
}
