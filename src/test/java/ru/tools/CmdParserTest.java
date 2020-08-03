package ru.tools;

import org.junit.Test;
import ru.pchelicam.tools.du.CmdParser;
import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Tests for CmdParser class
 */
public class CmdParserTest {
    @Test
    public void testNameFile1() {
        String parameter = "--td=YA --fu=\"Total Recall. My Unbelievably True Life Story Arnold Schwarzenegger.docx\" --tr=30 --mr=24 --cu=/Test/Test_file.txt --at=AbcDeFgHi12357";
        CmdParser cmdParser = new CmdParser();

        String resTd = cmdParser.parseParameterTd(parameter);
        assertEquals("YA", resTd);

        String resFu = cmdParser.parseParameterFu(parameter);
        assertEquals("Total Recall. My Unbelievably True Life Story Arnold Schwarzenegger.docx", resFu);

        String resMs = cmdParser.parseParameterTr(parameter);
        assertEquals("30", resMs);

        String resMd = cmdParser.parseParameterMr(parameter);
        assertEquals("24", resMd);

        String resCu = cmdParser.parseParameterCu(parameter);
        assertEquals("/Test/Test_file.txt", resCu);

        String resAt = cmdParser.parseParameterAt(parameter);
        assertEquals("AbcDeFgHi12357", resAt);
    }
    @Test
    public void testNameFile2() {
        String parameter = "--td=YA --fu=test.txt --tr=12 --mr=12 --cu=\"/all photos/photo.png\" --at=KlMnopqr1stUfG";
        CmdParser cmdParser = new CmdParser();

        String resFu = cmdParser.parseParameterFu(parameter);
        assertEquals("test.txt", resFu);

        String resMs = cmdParser.parseParameterTr(parameter);
        assertEquals("12", resMs);

        String resMd = cmdParser.parseParameterMr(parameter);
        assertEquals("12", resMd);

        String resCu = cmdParser.parseParameterCu(parameter);
        assertEquals("/all photos/photo.png", resCu);

        String resAt = cmdParser.parseParameterAt(parameter);
        assertEquals("KlMnopqr1stUfG", resAt);
    }
    @Test
    public void testNameFile3(){
        //incorrect test
        CmdParser cmdParser = new CmdParser();
        String parameter = "--fu=Энциклопедия жизни";
        String resFu = cmdParser.parseParameterFu(parameter);
        assertNotEquals("Энциклопедия жизни", resFu);
    }
}
