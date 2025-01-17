import static org.junit.Assert.assertEquals;

import org.junit.Test;

import java.sql.SQLOutput;
import java.util.Arrays;

public class BufferManagerTest {

    @Test
    public void testBufferManagerCommands() {
        BufferPool bufferPool = new BufferPool(); // Create BufferManager with buffer pool size of 3
        bufferPool.initialize(3);

        // Command 2, case 2
        assertEquals("Write was successful; Brought File 5 from disk; Placed in Frame 1", bufferPool.SET(430, "F05-Rec450, Jane Do, 10 Hill Rd, age020."));
        // Command 1, case 1
        assertEquals("F05-Rec450, Jane Do, 10 Hill Rd, age020.; File 5 already in memory; Located in Frame 1", bufferPool.GET(430));
        // Command 1, case 2
        assertEquals("F01-Rec020, Name020, address020, age020.; Brought file 1 from disk; Placed in Frame 2", bufferPool.GET(20));
        // Command 2, case 1
        assertEquals("Write was successful; File 5 already in memory; Located in Frame 1", bufferPool.SET(430,"F05-Rec450, John Do, 23 Lake Ln, age056."));
        // Command 3, case 1
        assertEquals("File 5 pinned in Frame 1; Not already pinned", bufferPool.PIN(5));
        // Command 4, case 2
        assertEquals("The corresponding block 3 cannot be unpinned because it is not in memory.", bufferPool.UNPIN(3));
        // Command 1, case 1
        assertEquals("F05-Rec450, John Do, 23 Lake Ln, age056.; File 5 already in memory; Located in Frame 1", bufferPool.GET(430));
        // Command 3, case 1
        assertEquals("File 5 pinned in Frame 1; Already pinned", bufferPool.PIN(5));
        // Command 1, case 2
        assertEquals("F07-Rec646, Name646, address646, age646.; Brought file 7 from disk; Placed in Frame 3", bufferPool.GET(646));
        // Command 3, case 2
        assertEquals("File 3 pinned in Frame 2; Not already pinned; Evicted file 1 from Frame 2", bufferPool.PIN(3));

        // Command 2, case 3
        assertEquals("Write was successful; Brought File 1 from disk; Placed in Frame 3; Evicted file 7 from Frame 3", bufferPool.SET(10, "F01-Rec010, Tim Boe, 09 Deer Dr, age009."));
        // Command 4, case 1
        assertEquals("File 1 in frame 3 is unpinned; Frame was already unpinned", bufferPool.UNPIN(1));
        // Command 1, case 3
        assertEquals("F04-Rec355, Name355, address355, age355.; Brought file 4 from disk; Placed in Frame 3; Evicted file 1 from frame 3", bufferPool.GET(355));

        // Command 3, case 2
        assertEquals("File 2 pinned in Frame 3; Not already pinned; Evicted file 4 from Frame 3", bufferPool.PIN(2));

        // Command 1, case 1
        assertEquals("F02-Rec156, Name156, address156, age156.; File 2 already in memory; Located in Frame 3", bufferPool.GET(156));

        // Command 2, case 4
        assertEquals("The corresponding block #1 cannot be accessed from disk because the memory buffers are full; Write was unsuccessful", bufferPool.SET(10,"F01-Rec010, No Work, 31 Hill St, age100."));

        // Command 3, case 3
        assertEquals("The corresponding block 7 cannot be pinned because the memory buffers are full", bufferPool.PIN(7));

        // Command 1, case 4
        assertEquals("The corresponding block #1 cannot be accessed from disk because the memory buffers are full", bufferPool.GET(10));

        // Command 4, case 1
        assertEquals("File 3 is unpinned in frame 2; Frame 2 was not already unpinned", bufferPool.UNPIN(3));

        // Command 4, case 1
        assertEquals("File 2 is unpinned in frame 3; Frame 3 was not already unpinned", bufferPool.UNPIN(2));

        // Command 1, case 3
        assertEquals("F01-Rec010, Tim Boe, 09 Deer Dr, age009.; Brought file 1 from disk; Placed in Frame 2; Evicted file 3 from frame 2", bufferPool.GET(10));

        // Command 3, case 2
        assertEquals("File 6 pinned in Frame 3; Not already pinned; Evicted file 2 from Frame 3", bufferPool.PIN(6));
    }
}
