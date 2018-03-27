package org.develnext.jphp.ext.semver.classes;

import org.develnext.jphp.ext.semver.SemverJvmTestCase;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.runners.MethodSorters;

@RunWith(JUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SemVersionTest extends SemverJvmTestCase {
    @Test
    public void testBasic() {
        check("semver/semver_basic.php");
    }

    @Test
    public void testCompare() {
        check("semver/semver_compare.php");
    }

    @Test
    public void testSatisfies() {
        check("semver/semver_satisfies.php");
    }

    @Test
    public void testSnapshots() {
        check("semver/semver_snapshots.php");
    }
}
