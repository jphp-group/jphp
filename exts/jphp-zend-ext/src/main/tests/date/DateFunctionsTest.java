package date;

import org.develnext.jphp.zend.ZendJvmTestCase;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.runners.MethodSorters;

@RunWith(JUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DateFunctionsTest extends ZendJvmTestCase {
    @Test
    public void testcheckdate() {
        check("ext/date/checkdate_basic1.phpt");
        check("ext/date/006.phpt");
    }

    @Test
    public void testgetdate() {
        check("ext/date/008.phpt");
    }

    @Test
    public void teststrftime() {

        // check("ext/date/gmstrftime_variation3.phpt");

        // prinf or sscanf bug
        // check("ext/date/gmstrftime_variation15.phpt");
        // check("ext/date/gmstrftime_variation16.phpt");

        check("ext/date/gmstrftime_variation5.phpt");
        check("ext/date/009.phpt");
        check("ext/date/gmstrftime_basic.phpt");
        check("ext/date/gmstrftime_variation20.phpt");
        check("ext/date/gmstrftime_variation4.phpt");
        check("ext/date/gmstrftime_variation6.phpt");
        check("ext/date/gmstrftime_variation7.phpt");
        check("ext/date/gmstrftime_variation8.phpt");
        check("ext/date/gmstrftime_variation9.phpt");
        check("ext/date/gmstrftime_variation10.phpt");
        check("ext/date/gmstrftime_variation11.phpt");
        check("ext/date/gmstrftime_variation13.phpt");
        check("ext/date/gmstrftime_variation14.phpt");
        check("ext/date/gmstrftime_variation17.phpt");
        check("ext/date/gmstrftime_variation18.phpt");
        check("ext/date/gmstrftime_variation19.phpt");
        check("ext/date/gmstrftime_variation21.phpt");
        check("ext/date/gmstrftime_variation22.phpt");
    }

    @Test
    public void testlocaltime() {
        check("ext/date/007.phpt");
        check("ext/date/localtime_basic.phpt");
        check("ext/date/localtime_variation4.phpt");
        check("ext/date/localtime_variation5.phpt");
    }

    @Test
    public void testDate() {
        check("ext/date/003.phpt");
        check("ext/date/004.phpt");
    }

    @Test
    public void testDateDefaultTimezoneGet() {
        check("ext/date/date_default_timezone_get-1.phpt");
        check("ext/date/date_default_timezone_get-2.phpt");
        check("ext/date/date_default_timezone_get-3.phpt");
        check("ext/date/date_default_timezone_get-4.phpt");
    }

    @Test
    public void testMkTime() {
        check("ext/date/bug21966.phpt");
        check("ext/date/mktime-1.phpt");
        check("ext/date/mktime_basic1.phpt");
        check("ext/date/mktime-3-64bit.phpt");
        check("ext/date/mktime_no_args.phpt");
        check("ext/date/gmmktime_basic.phpt");
    }

    @Test
    public void testDateCreate() {
        check("ext/date/bug36599.phpt");
        check("ext/date/date_create-relative.phpt");
        check("ext/date/date_create-2.phpt");
        check("ext/date/date_create_basic.phpt");
        check("ext/date/date_create_from_format_basic2.phpt");
        //check("ext/date/date_create-1.phpt");
    }

    @Test
    public void strtotime() {
        check("ext/date/002.phpt");
        check("ext/date/strtotime.phpt");
        check("ext/date/strtotime2.phpt");
        check("ext/date/strtotime3-64bit.phpt");
        check("ext/date/strtotime-mysql-64bit.phpt");
        check("ext/date/strtotime_basic.phpt");
        check("ext/date/strtotime_basic2.phpt");
        check("ext/date/strtotime-relative.phpt");
        check("ext/date/bug26198.phpt");
        check("ext/date/bug13142.phpt");
        check("ext/date/bug14561.phpt");
        check("ext/date/bug17988.phpt");
        check("ext/date/bug21399.phpt");
        check("ext/date/bug26320.phpt");
        check("ext/date/bug28088.phpt");
        check("ext/date/bug28599.phpt");
        check("ext/date/bug29150.phpt");
        check("ext/date/bug29595.phpt");
        check("ext/date/bug30096.phpt");
        check("ext/date/bug32588.phpt");
        check("ext/date/bug33056.phpt");
        check("ext/date/bug33452.phpt");
        check("ext/date/bug33532.phpt");
        check("ext/date/bug32555.phpt");
        check("ext/date/bug32555.phpt");
        check("ext/date/bug32270.phpt");
        check("ext/date/bug26090.phpt");
        check("ext/date/bug26694.phpt");
        check("ext/date/bug26317.phpt");
        check("ext/date/bug27780.phpt");
        check("ext/date/bug28024.phpt");
        check("ext/date/bug32086.phpt");
        check("ext/date/bug33414-1.phpt");
        check("ext/date/bug33415-1.phpt");
        check("ext/date/bug33414-1.phpt");
        check("ext/date/bug33414-2.phpt");
        check("ext/date/bug33415-2.phpt");
        check("ext/date/bug20382-1.phpt");
        check("ext/date/bug30532.phpt");
        check("ext/date/bug33536.phpt");
        check("ext/date/bug33562.phpt");
        check("ext/date/bug33563.phpt");
        check("ext/date/bug33869.phpt");
        check("ext/date/bug33957.phpt");
        check("ext/date/bug33536.phpt");
        check("ext/date/bug33562.phpt");
        check("ext/date/bug33563.phpt");
        check("ext/date/bug33869.phpt");
        check("ext/date/bug33957.phpt");
        check("ext/date/bug34676.phpt");
        check("ext/date/bug34771.phpt");
        check("ext/date/bug35422.phpt");
        check("ext/date/bug35624.phpt");
        check("ext/date/bug35630.phpt");
        check("ext/date/bug35699.phpt");
        check("ext/date/bug35705.phpt");
        check("ext/date/bug35885.phpt");
        check("ext/date/bug36224.phpt");
        check("ext/date/bug36510.phpt");

        // fail
        check("ext/date/bug34087.phpt");
        check("ext/date/bug33578.phpt");
        check("ext/date/bug29585.phpt");
    }

    @Test
    public void strtotimeFails() {
        //check("ext/date/bug34304.phpt");
        //check("ext/date/bug35143.phpt");
        //check("ext/date/bug35218.phpt");
        //check("ext/date/bug35414.phpt");
        //check("ext/date/bug35425.phpt");
        //check("ext/date/bug35456.phpt");
        //check("ext/date/bug35499.phpt");
        //check("ext/date/bug35887.phpt");

        check("ext/date/bug36988.phpt");
        check("ext/date/bug37017.phpt");
        check("ext/date/bug37368.phpt");
        check("ext/date/bug37514.phpt");
        check("ext/date/bug37616.phpt");
        check("ext/date/bug37747.phpt");
        check("ext/date/bug38229.phpt");
        check("ext/date/bug39782.phpt");
        check("ext/date/bug40743.phpt");
        check("ext/date/bug40861.phpt");
        check("ext/date/bug41523.phpt");
        check("ext/date/bug41523-64bit.phpt");
        check("ext/date/bug41599.phpt");
        check("ext/date/bug41709.phpt");
        check("ext/date/bug41842.phpt");
        check("ext/date/bug41844.phpt");
        check("ext/date/bug41964.phpt");
        check("ext/date/bug42910.phpt");
        check("ext/date/bug43003.phpt");
        check("ext/date/bug43075.phpt");
        check("ext/date/bug43452.phpt");
        check("ext/date/bug43527.phpt");
        check("ext/date/bug43808.phpt");
        check("ext/date/bug43960.phpt");
        check("ext/date/bug44562.phpt");
        check("ext/date/bug44742.phpt");
        check("ext/date/bug44780.phpt");
        check("ext/date/bug45081.phpt");
        check("ext/date/bug45529.phpt");
        check("ext/date/bug45543.phpt");
        check("ext/date/bug45554.phpt");
        check("ext/date/bug45682.phpt");
        check("ext/date/bug45866.phpt");
        check("ext/date/bug46108.phpt");
        check("ext/date/bug46111.phpt");
        check("ext/date/bug46268.phpt");
        check("ext/date/bug46874.phpt");
        check("ext/date/bug48058.phpt");
        check("ext/date/bug48097.phpt");
        check("ext/date/bug48187.phpt");
        check("ext/date/bug48276.phpt");
        check("ext/date/bug48476.phpt");
        check("ext/date/bug48678.phpt");
        check("ext/date/bug49059.phpt");
        check("ext/date/bug49081.phpt");
        check("ext/date/bug49585.phpt");
        check("ext/date/bug49700.phpt");
        check("ext/date/bug49778.phpt");
        check("ext/date/bug50055.phpt");
        check("ext/date/bug50392.phpt");
        check("ext/date/bug50475.phpt");
        check("ext/date/bug50680.phpt");
        check("ext/date/bug51096.phpt");
        check("ext/date/bug51393.phpt");
        check("ext/date/bug51819.phpt");
        check("ext/date/bug51866.phpt");
        check("ext/date/bug51994.phpt");
        check("ext/date/bug52062.phpt");
        check("ext/date/bug52062-64bit.phpt");
        check("ext/date/bug52063.phpt");
        check("ext/date/bug52113.phpt");
        check("ext/date/bug52290.phpt");
        check("ext/date/bug52342.phpt");
        check("ext/date/bug52430.phpt");
        check("ext/date/bug52454.phpt");
        check("ext/date/bug52480.phpt");
        check("ext/date/bug52577.phpt");
        check("ext/date/bug52668.phpt");
        check("ext/date/bug52738.phpt");
        check("ext/date/bug52808.phpt");
        check("ext/date/bug53437.phpt");
        check("ext/date/bug53437_var1.phpt");
        check("ext/date/bug53437_var2.phpt");
        check("ext/date/bug53437_var3.phpt");
        check("ext/date/bug53437_var4.phpt");
        check("ext/date/bug53437_var5.phpt");
        check("ext/date/bug53437_var6.phpt");
        check("ext/date/bug53502.phpt");
        check("ext/date/bug53879.phpt");
        check("ext/date/bug54283.phpt");
        check("ext/date/bug54316.phpt");
        check("ext/date/bug54340.phpt");
        check("ext/date/bug54597.phpt");
        check("ext/date/bug54851.phpt");
        check("ext/date/bug55253.phpt");
        check("ext/date/bug55397.phpt");
        check("ext/date/bug55407.phpt");
        check("ext/date/bug60236.phpt");
        check("ext/date/bug60774.phpt");
        check("ext/date/bug61642.phpt");
        check("ext/date/bug62500.phpt");
        check("ext/date/bug62561.phpt");
        check("ext/date/bug62852.phpt");
        check("ext/date/bug62852_var2.phpt");
        check("ext/date/bug62852_var3.phpt");
        check("ext/date/bug62896.phpt");
        check("ext/date/bug63391.phpt");
        check("ext/date/bug63435.phpt");
        check("ext/date/bug63740.phpt");
        check("ext/date/bug64157.phpt");
        check("ext/date/bug64887.phpt");
        check("ext/date/bug65184.phpt");
        check("ext/date/bug65371.phpt");
        check("ext/date/bug65502.phpt");
        check("ext/date/bug65548.phpt");
        check("ext/date/bug66721.phpt");
        check("ext/date/bug66836.phpt");
        check("ext/date/bug66985.phpt");
        check("ext/date/bug67109.phpt");
        check("ext/date/bug67118.phpt");
        check("ext/date/bug67118_2.phpt");
        check("ext/date/bug67251.phpt");
        check("ext/date/bug67253.phpt");
        check("ext/date/bug67308.phpt");
        check("ext/date/bug68078.phpt");
        check("ext/date/bug68078_negative.phpt");
        check("ext/date/bug68406.phpt");
        check("ext/date/bug68669.phpt");
        check("ext/date/bug68942.phpt");
        check("ext/date/bug68942_2.phpt");
        check("ext/date/bug69089.phpt");
        check("ext/date/bug69336.phpt");
        check("ext/date/bug69587.phpt");
        check("ext/date/bug70245.phpt");
        check("ext/date/bug70266.phpt");
        check("ext/date/bug70277.phpt");
        check("ext/date/bug71525.phpt");
        check("ext/date/bug71635.phpt");
        check("ext/date/bug71889.phpt");
        check("ext/date/bug72096.phpt");
        check("ext/date/bug72719.phpt");
        check("ext/date/bug73091.phpt");
        check("ext/date/bug73294.phpt");
        check("ext/date/bug73426.phpt");
        check("ext/date/bug73489.phpt");
        check("ext/date/bug73837.phpt");
        check("ext/date/bug73858.phpt");
        check("ext/date/bug73942.phpt");
        check("ext/date/bug74057.phpt");
        check("ext/date/bug74080.phpt");
        check("ext/date/bug74639.phpt");
        check("ext/date/bug74652.phpt");
        check("ext/date/bug74852.phpt");
        check("ext/date/bug75002.phpt");
        check("ext/date/bug75222.phpt");
        check("ext/date/bug75232.phpt");
        check("ext/date/bug75577.phpt");
        check("ext/date/bug75851.phpt");
        check("ext/date/bug75857.phpt");
    }

    @Test
    public void testDateDateSet() {
        check("ext/date/013.phpt");
    }

    @Test
    @Ignore
    public void testDateDefaultTimezoneSet() {
        check("ext/date/date_default_timezone_set-1.phpt");
        check("ext/date/date_default_timezone_set_error.phpt");
    }

    @Test
    public void testTimezoneAbbreviationsList() {
        check("ext/date/010.phpt");
    }

    @Test
    @Ignore
    public void testTimezoneNameFromAbbr() {
        check("ext/date/011.phpt");
    }
}
