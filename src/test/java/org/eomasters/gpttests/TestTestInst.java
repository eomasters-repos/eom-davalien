package org.eomasters.gpttests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.regex.MatchResult;
import org.junit.jupiter.api.Test;

class TestTestInst {

  @Test
  void testMatchResourceVariable() {
    List<MatchResult> matchResults = TestInst.getMatchResults("{GPH:S2_subset_resampleGraph} -Pinput={SRC:S2_MSI}");
    assertEquals(2, matchResults.size());
    String[] firstMatch = TestInst.getTokens(matchResults.get(0));
    assertEquals(3, firstMatch.length);
    assertEquals("{GPH:S2_subset_resampleGraph}", firstMatch[0]);
    assertEquals("GPH", firstMatch[1]);
    assertEquals("S2_subset_resampleGraph", firstMatch[2]);
    String[] secondMatch = TestInst.getTokens(matchResults.get(1));
    assertEquals(3, secondMatch.length);
    assertEquals("{SRC:S2_MSI}", secondMatch[0]);
    assertEquals("SRC", secondMatch[1]);
    assertEquals("S2_MSI", secondMatch[2]);

  }
}