package com.example.myruns;


// Generated with Weka 3.8.4
//
// This code is public domain and comes with no warranty.
//
// Timestamp: Mon Feb 17 14:58:29 EST 2020


public class WekaClassifier {

    public static double classify(Object[] i)
            throws Exception {

        double p = Double.NaN;
        p = WekaClassifier.N3c23e0730(i);
        return p;
    }

    static double N3c23e0730(Object[] i) {
        double p = Double.NaN;
        if (i[0] == null) {
            p = 0;
        } else if (((Double) i[0]).doubleValue() <= 85.441553) {
            p = WekaClassifier.N6e92a8d1(i);
        } else if (((Double) i[0]).doubleValue() > 85.441553) {
            p = WekaClassifier.Nb9e828f6(i);
        }
        return p;
    }

    static double N6e92a8d1(Object[] i) {
        double p = Double.NaN;
        if (i[17] == null) {
            p = 0;
        } else if (((Double) i[17]).doubleValue() <= 0.593241) {
            p = 0;
        } else if (((Double) i[17]).doubleValue() > 0.593241) {
            p = WekaClassifier.N36b5aca72(i);
        }
        return p;
    }

    static double N36b5aca72(Object[] i) {
        double p = Double.NaN;
        if (i[14] == null) {
            p = 0;
        } else if (((Double) i[14]).doubleValue() <= 1.078705) {
            p = WekaClassifier.N7c2467123(i);
        } else if (((Double) i[14]).doubleValue() > 1.078705) {
            p = 0;
        }
        return p;
    }

    static double N7c2467123(Object[] i) {
        double p = Double.NaN;
        if (i[23] == null) {
            p = 0;
        } else if (((Double) i[23]).doubleValue() <= 0.546725) {
            p = WekaClassifier.N5f27209d4(i);
        } else if (((Double) i[23]).doubleValue() > 0.546725) {
            p = 1;
        }
        return p;
    }

    static double N5f27209d4(Object[] i) {
        double p = Double.NaN;
        if (i[8] == null) {
            p = 1;
        } else if (((Double) i[8]).doubleValue() <= 0.807577) {
            p = 1;
        } else if (((Double) i[8]).doubleValue() > 0.807577) {
            p = WekaClassifier.N1055c4b65(i);
        }
        return p;
    }

    static double N1055c4b65(Object[] i) {
        double p = Double.NaN;
        if (i[17] == null) {
            p = 0;
        } else if (((Double) i[17]).doubleValue() <= 1.004893) {
            p = 0;
        } else if (((Double) i[17]).doubleValue() > 1.004893) {
            p = 1;
        }
        return p;
    }

    static double Nb9e828f6(Object[] i) {
        double p = Double.NaN;
        if (i[0] == null) {
            p = 1;
        } else if (((Double) i[0]).doubleValue() <= 800.159319) {
            p = WekaClassifier.N545d68197(i);
        } else if (((Double) i[0]).doubleValue() > 800.159319) {
            p = 2;
        }
        return p;
    }

    static double N545d68197(Object[] i) {
        double p = Double.NaN;
        if (i[64] == null) {
            p = 1;
        } else if (((Double) i[64]).doubleValue() <= 6.426661) {
            p = WekaClassifier.N329899c78(i);
        } else if (((Double) i[64]).doubleValue() > 6.426661) {
            p = WekaClassifier.N21af638e18(i);
        }
        return p;
    }

    static double N329899c78(Object[] i) {
        double p = Double.NaN;
        if (i[9] == null) {
            p = 1;
        } else if (((Double) i[9]).doubleValue() <= 3.787761) {
            p = WekaClassifier.Nc569b569(i);
        } else if (((Double) i[9]).doubleValue() > 3.787761) {
            p = WekaClassifier.N21507bb13(i);
        }
        return p;
    }

    static double Nc569b569(Object[] i) {
        double p = Double.NaN;
        if (i[28] == null) {
            p = 2;
        } else if (((Double) i[28]).doubleValue() <= 0.135904) {
            p = 2;
        } else if (((Double) i[28]).doubleValue() > 0.135904) {
            p = WekaClassifier.N7323faa910(i);
        }
        return p;
    }

    static double N7323faa910(Object[] i) {
        double p = Double.NaN;
        if (i[11] == null) {
            p = 1;
        } else if (((Double) i[11]).doubleValue() <= 1.464456) {
            p = 1;
        } else if (((Double) i[11]).doubleValue() > 1.464456) {
            p = WekaClassifier.N4c01497411(i);
        }
        return p;
    }

    static double N4c01497411(Object[] i) {
        double p = Double.NaN;
        if (i[11] == null) {
            p = 2;
        } else if (((Double) i[11]).doubleValue() <= 1.860763) {
            p = WekaClassifier.N3aa1b79212(i);
        } else if (((Double) i[11]).doubleValue() > 1.860763) {
            p = 1;
        }
        return p;
    }

    static double N3aa1b79212(Object[] i) {
        double p = Double.NaN;
        if (i[0] == null) {
            p = 1;
        } else if (((Double) i[0]).doubleValue() <= 134.665211) {
            p = 1;
        } else if (((Double) i[0]).doubleValue() > 134.665211) {
            p = 2;
        }
        return p;
    }

    static double N21507bb13(Object[] i) {
        double p = Double.NaN;
        if (i[0] == null) {
            p = 0;
        } else if (((Double) i[0]).doubleValue() <= 171.844891) {
            p = WekaClassifier.N6e8f53e214(i);
        } else if (((Double) i[0]).doubleValue() > 171.844891) {
            p = WekaClassifier.N1f29c20b16(i);
        }
        return p;
    }

    static double N6e8f53e214(Object[] i) {
        double p = Double.NaN;
        if (i[14] == null) {
            p = 1;
        } else if (((Double) i[14]).doubleValue() <= 0.969168) {
            p = WekaClassifier.N2883edae15(i);
        } else if (((Double) i[14]).doubleValue() > 0.969168) {
            p = 0;
        }
        return p;
    }

    static double N2883edae15(Object[] i) {
        double p = Double.NaN;
        if (i[0] == null) {
            p = 1;
        } else if (((Double) i[0]).doubleValue() <= 131.609433) {
            p = 1;
        } else if (((Double) i[0]).doubleValue() > 131.609433) {
            p = 2;
        }
        return p;
    }

    static double N1f29c20b16(Object[] i) {
        double p = Double.NaN;
        if (i[22] == null) {
            p = 0;
        } else if (((Double) i[22]).doubleValue() <= 0.437715) {
            p = 0;
        } else if (((Double) i[22]).doubleValue() > 0.437715) {
            p = WekaClassifier.N352b368a17(i);
        }
        return p;
    }

    static double N352b368a17(Object[] i) {
        double p = Double.NaN;
        if (i[0] == null) {
            p = 1;
        } else if (((Double) i[0]).doubleValue() <= 238.190643) {
            p = 1;
        } else if (((Double) i[0]).doubleValue() > 238.190643) {
            p = 2;
        }
        return p;
    }

    static double N21af638e18(Object[] i) {
        double p = Double.NaN;
        if (i[7] == null) {
            p = 1;
        } else if (((Double) i[7]).doubleValue() <= 60.560663) {
            p = WekaClassifier.N16f6048819(i);
        } else if (((Double) i[7]).doubleValue() > 60.560663) {
            p = WekaClassifier.N354f141229(i);
        }
        return p;
    }

    static double N16f6048819(Object[] i) {
        double p = Double.NaN;
        if (i[27] == null) {
            p = 1;
        } else if (((Double) i[27]).doubleValue() <= 4.098532) {
            p = 1;
        } else if (((Double) i[27]).doubleValue() > 4.098532) {
            p = WekaClassifier.N479ea5c720(i);
        }
        return p;
    }

    static double N479ea5c720(Object[] i) {
        double p = Double.NaN;
        if (i[28] == null) {
            p = 2;
        } else if (((Double) i[28]).doubleValue() <= 3.460611) {
            p = 2;
        } else if (((Double) i[28]).doubleValue() > 3.460611) {
            p = WekaClassifier.N2b6d54d821(i);
        }
        return p;
    }

    static double N2b6d54d821(Object[] i) {
        double p = Double.NaN;
        if (i[19] == null) {
            p = 1;
        } else if (((Double) i[19]).doubleValue() <= 20.859795) {
            p = WekaClassifier.N7104f12822(i);
        } else if (((Double) i[19]).doubleValue() > 20.859795) {
            p = 2;
        }
        return p;
    }

    static double N7104f12822(Object[] i) {
        double p = Double.NaN;
        if (i[12] == null) {
            p = 1;
        } else if (((Double) i[12]).doubleValue() <= 13.468292) {
            p = WekaClassifier.N577079c423(i);
        } else if (((Double) i[12]).doubleValue() > 13.468292) {
            p = 1;
        }
        return p;
    }

    static double N577079c423(Object[] i) {
        double p = Double.NaN;
        if (i[0] == null) {
            p = 1;
        } else if (((Double) i[0]).doubleValue() <= 674.405488) {
            p = WekaClassifier.N65725e7024(i);
        } else if (((Double) i[0]).doubleValue() > 674.405488) {
            p = 2;
        }
        return p;
    }

    static double N65725e7024(Object[] i) {
        double p = Double.NaN;
        if (i[16] == null) {
            p = 2;
        } else if (((Double) i[16]).doubleValue() <= 6.864265) {
            p = WekaClassifier.N24f81d9225(i);
        } else if (((Double) i[16]).doubleValue() > 6.864265) {
            p = WekaClassifier.N26c9dc5d27(i);
        }
        return p;
    }

    static double N24f81d9225(Object[] i) {
        double p = Double.NaN;
        if (i[15] == null) {
            p = 1;
        } else if (((Double) i[15]).doubleValue() <= 6.750311) {
            p = WekaClassifier.N72a3359426(i);
        } else if (((Double) i[15]).doubleValue() > 6.750311) {
            p = 2;
        }
        return p;
    }

    static double N72a3359426(Object[] i) {
        double p = Double.NaN;
        if (i[0] == null) {
            p = 2;
        } else if (((Double) i[0]).doubleValue() <= 259.157597) {
            p = 2;
        } else if (((Double) i[0]).doubleValue() > 259.157597) {
            p = 1;
        }
        return p;
    }

    static double N26c9dc5d27(Object[] i) {
        double p = Double.NaN;
        if (i[10] == null) {
            p = 1;
        } else if (((Double) i[10]).doubleValue() <= 16.838868) {
            p = 1;
        } else if (((Double) i[10]).doubleValue() > 16.838868) {
            p = WekaClassifier.N513f972a28(i);
        }
        return p;
    }

    static double N513f972a28(Object[] i) {
        double p = Double.NaN;
        if (i[0] == null) {
            p = 2;
        } else if (((Double) i[0]).doubleValue() <= 571.96104) {
            p = 2;
        } else if (((Double) i[0]).doubleValue() > 571.96104) {
            p = 1;
        }
        return p;
    }

    static double N354f141229(Object[] i) {
        double p = Double.NaN;
        if (i[1] == null) {
            p = 1;
        } else if (((Double) i[1]).doubleValue() <= 154.359949) {
            p = WekaClassifier.N5e2b98ba30(i);
        } else if (((Double) i[1]).doubleValue() > 154.359949) {
            p = 2;
        }
        return p;
    }

    static double N5e2b98ba30(Object[] i) {
        double p = Double.NaN;
        if (i[64] == null) {
            p = 2;
        } else if (((Double) i[64]).doubleValue() <= 28.908083) {
            p = 2;
        } else if (((Double) i[64]).doubleValue() > 28.908083) {
            p = WekaClassifier.N36e89c1631(i);
        }
        return p;
    }

    static double N36e89c1631(Object[] i) {
        double p = Double.NaN;
        if (i[4] == null) {
            p = 1;
        } else if (((Double) i[4]).doubleValue() <= 147.654589) {
            p = 1;
        } else if (((Double) i[4]).doubleValue() > 147.654589) {
            p = WekaClassifier.N1b43db2632(i);
        }
        return p;
    }

    static double N1b43db2632(Object[] i) {
        double p = Double.NaN;
        if (i[14] == null) {
            p = 1;
        } else if (((Double) i[14]).doubleValue() <= 16.68769) {
            p = 1;
        } else if (((Double) i[14]).doubleValue() > 16.68769) {
            p = 2;
        }
        return p;
    }
}
