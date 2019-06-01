package com.pixcat.voxel;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

import static org.junit.Assert.*;

public class Cord3IntTest {

    @Test
    public void testValidCreation(){
        Coord3Int coord = new Coord3Int(0,0,0);
        assertTrue(coord.x == 0);
        assertTrue(coord.y == 0);
        assertTrue(coord.z == 0);
    }

    @Test
    public void testEqualsHashCode() {
        EqualsVerifier.forClass(Coord3Int.class).verify();
    }

}
