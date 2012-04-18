/*
 * Copyright (c) 2007-2012 The Broad Institute, Inc.
 * SOFTWARE COPYRIGHT NOTICE
 * This software and its documentation are the copyright of the Broad Institute, Inc. All rights are reserved.
 *
 * This software is supplied without any warranty or guaranteed support whatsoever. The Broad Institute is not responsible for its use, misuse, or functionality.
 *
 * This software is licensed under the terms of the GNU Lesser General Public License (LGPL),
 * Version 2.1 which is available at http://www.opensource.org/licenses/lgpl-2.1.php.
 */

package org.broad.igv.gwas;

import org.broad.igv.exceptions.ParserException;
import org.broad.igv.feature.genome.Genome;
import org.broad.igv.util.ResourceLocator;
import org.broad.igv.util.TestUtils;
import org.broad.igv.util.collections.FloatArrayList;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedHashMap;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

/**
 * User: jacob
 * Date: 2012/04/18
 */
public class GWASParserTest {
    Genome genome;

    @Before
    public void setUp() throws Exception {
        TestUtils.setUpHeadless();
        genome = TestUtils.loadGenome();
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testParse() throws Exception {
        GWASParser parser = new GWASParser(new ResourceLocator(TestUtils.DATA_DIR + "gwas/smallp.gwas"), genome);
        GWASData data = parser.parse();
        LinkedHashMap<String, FloatArrayList> values = data.getValues();
        for (String chr : values.keySet()) {
            FloatArrayList floats = values.get(chr);
            for (int ff = 0; ff < floats.size(); ff++) {
                float val = floats.get(ff);
                assertFalse("Value is infinite", Float.isInfinite(val));
                assertFalse("Value isNan", Float.isNaN(val));
                assertFalse("Value is 0", val == 0.0f);
            }
        }
    }

    @Test
    public void testBadPs() throws Exception {
        String[] finames = {"badp_neg.gwas", "badp_text.gwas", "badp_zero.gwas"};
        for (String finame : finames) {
            tstParseBad(finame);
        }
    }

    public void tstParseBad(String finame) throws Exception {
        GWASParser parser = new GWASParser(new ResourceLocator(TestUtils.DATA_DIR + "gwas/" + finame), genome);
        boolean excepted = false;
        try {
            GWASData data = parser.parse();
        } catch (ParserException e) {
            excepted = true;
        }
        assertTrue(excepted);

    }
}