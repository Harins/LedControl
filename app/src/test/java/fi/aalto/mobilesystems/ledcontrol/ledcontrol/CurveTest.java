package fi.aalto.mobilesystems.ledcontrol.ledcontrol;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class CurveTest {
    @Test
    public void testRefined() throws Exception {
        // need to build proper test data for this
        Curve c = new Curve();
        c.addPoint(new PointF(0.0f, 0.0f));
        c.addPoint(new PointF(1.0f, 1.1f));
        c.addPoint(new PointF(0.0f, 2.0f));
        Curve c2 = c.refined(100);
        List<PointF> ls = c2.getPoints();
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append("{");
        for (PointF p : ls) {
            strBuilder.append("{");
            strBuilder.append(p.x);
            strBuilder.append(",");
            strBuilder.append(p.y);
            strBuilder.append("},");
        }
        strBuilder.deleteCharAt(strBuilder.length()-1);
        strBuilder.append("}");
        String res = strBuilder.toString();
        System.out.println(res);
    }

    @RunWith(Parameterized.class)
    public static class PointOnCurveTest {
        @Parameterized.Parameters
        public static Iterable<Object[][]> data() {
            return Arrays.asList(new Object[][][]{
                    { {0.0, 0.0f, 0.0f}, {0.0f, 0.0f, 1.0f, 1.0f, 2.0f, 2.0f}},
                    { {0.5, 1.0f, 1.0f}, {0.0f, 0.0f, 1.0f, 1.0f, 2.0f, 2.0f}},
                    { {1.0, 2.0f, 2.0f}, {0.0f, 0.0f, 1.0f, 1.0f, 2.0f, 2.0f}},
                    { {1.0, 0.0f, 0.0f},
                            {
                            0.0f, 0.0f, 0.0f, 4.0f, 4.0f, 4.0f, 4.0f, 0.0f, 0.0f, 0.0f
                            }
                    },
                    { {0.5, 4.0f, 4.0f},
                            {
                                    0.0f, 0.0f, 0.0f, 4.0f, 4.0f, 4.0f, 4.0f, 0.0f, 0.0f, 0.0f
                            }
                    },
                    { {0.625, 4.0f, 2.0f},
                            {
                                    0.0f, 0.0f, 0.0f, 4.0f, 4.0f, 4.0f, 4.0f, 0.0f, 0.0f, 0.0f
                            }
                    }});
        }

        private Curve curve;
        private double ratio;
        private PointF expected;

        public PointOnCurveTest(Object[] input, Object[] points) {
            this.curve = new Curve();
            this.ratio = (double)input[0];
            this.expected = new PointF((float)input[1], (float)input[2]);
            float last = 0.0f;
            int i = 0;
            for (Object n : points) {
                if (i % 2 == 0)
                    last = (float)n;
                else {
                    this.curve.addPoint(new PointF(last, (float)n));
                }
                i++;
            }
        }

        @Test
        public void testPointOnCurve() {
            PointF actual = this.curve.getPointOnCurve(ratio);
            assertTrue(actual.equals(expected));
        }
    }
}