package org.develnext.jphp.zend.ext.support;

import java.util.Comparator;

public class NaturalOrderComparator implements Comparator
{
    protected final boolean ignoreCase;
    protected final boolean revert;

    public NaturalOrderComparator(boolean ignoreCase, boolean revert) {
        this.ignoreCase = ignoreCase;
        this.revert = revert;
    }

    int compareRight(String a, String b)
    {
        int bias = 0;
        int ia = 0;
        int ib = 0;

        // The longest run of digits wins. That aside, the greatest
        // value wins, but we can't know that it will until we've scanned
        // both numbers to know that they have the same magnitude, so we
        // remember it in BIAS.
        for (;; ia++, ib++)
        {
            char ca = charAt(a, ia);
            char cb = charAt(b, ib);

            if (!Character.isDigit(ca) && !Character.isDigit(cb))
            {
                return bias;
            }
            else if (!Character.isDigit(ca))
            {
                return -1;
            }
            else if (!Character.isDigit(cb))
            {
                return +1;
            }
            else if (ca < cb)
            {
                if (bias == 0)
                {
                    bias = -1;
                }
            }
            else if (ca > cb)
            {
                if (bias == 0)
                    bias = +1;
            }
            else if (ca == 0 && cb == 0)
            {
                return bias;
            }
        }
    }

    @Override
    public int compare(Object o1, Object o2)
    {
        String a = o1.toString();
        String b = o2.toString();

        int ia = 0, ib = 0;
        int nza = 0, nzb = 0;
        char ca, cb;
        int result;

        while (true)
        {
            // only count the number of zeroes leading the last number compared
            nza = nzb = 0;

            ca = charAt(a, ia);
            cb = charAt(b, ib);

            // skip over leading spaces or zeros
            while (Character.isSpaceChar(ca) || ca == '0')
            {
                if (ca == '0')
                {
                    nza++;
                }
                else
                {
                    // only count consecutive zeroes
                    nza = 0;
                }

                ca = charAt(a, ++ia);
            }

            while (Character.isSpaceChar(cb) || cb == '0')
            {
                if (cb == '0')
                {
                    nzb++;
                }
                else
                {
                    // only count consecutive zeroes
                    nzb = 0;
                }

                cb = charAt(b, ++ib);
            }

            // process run of digits
            if (Character.isDigit(ca) && Character.isDigit(cb))
            {
                if ((result = compareRight(a.substring(ia), b.substring(ib))) != 0)
                {
                    return result * (revert ? -1 : 1);
                }
            }

            if (ca == 0 && cb == 0)
            {
                // The strings compare the same. Perhaps the caller
                // will want to call strcmp to break the tie.
                return (nza - nzb) * (revert ? -1 : 1);
            }

            if (ca < cb)
            {
                return -1 * (revert ? -1 : 1);
            }
            else if (ca > cb)
            {
                return +1 * (revert ? -1 : 1);
            }

            ++ia;
            ++ib;
        }
    }

    char charAt(String s, int i)
    {
        if (i >= s.length())
        {
            return 0;
        }
        else
        {
            char c = s.charAt(i);

            if (this.ignoreCase) {
                c = Character.toLowerCase(c);
            }

            return c;
        }
    }
}