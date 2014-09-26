/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package NPOCR;

/**
 *
 * @author rupak
 */
public class NEPClassifier {

    public static String strValue;

//    public static void main(String[] args) {
//        System.out.println(ClassifyDC(29, 13, 400, 200));
//    }

    public static String ClassifyDecisionTree(int Width, int Height, int BB_Area, int Black_Pixels_Count) {
        if (Width <= 28) {
            if (Width <= 23) {
                if (BB_Area <= 496) {
                    if (Height <= 13) {
                        strValue = "five_one_four_(35.0)";
                    } else if (Height > 13) {
                        strValue = "five_one_three_(35.0)";
                    }
                } else if (BB_Area > 496) {
                    if (Height <= 34) {
                        strValue = "five_zero_four_(35.0)";
                    } else if (Height > 34) {
                        strValue = "five_one_zero_(35.0)";
                    }
                }
            } else if (Width > 23) {
                if (Height <= 35) {
                    if (Black_Pixels_Count <= 230) {
                        if (Width <= 27) {
                            if (Height <= 31) {
                                strValue = "five_zero_nine_(46.0 / 13.0)";
                            } else if (Height > 31) {
                                strValue = "five_zero_three_(5.0)";
                            }
                        } else if (Width > 27) {
                            if (Black_Pixels_Count <= 226) {
                                strValue = "five_zero_two_(5.0 / 2.0)";
                            } else if (Black_Pixels_Count > 226) {
                                strValue = "five_zero_nine_(2.0)";
                            }
                        }
                    } else if (Black_Pixels_Count > 230) {
                        strValue = "five_zero_three_(15.0)";
                    }
                } else if (Height > 35) {
                    strValue = "five_one_two_(35.0)";
                }
            }
        } else if (Width > 28) {
            if (Width <= 33) {
                if (Width <= 31) {
                    if (Width <= 30) {
                        if (Black_Pixels_Count <= 246) {
                            if (Width <= 29) {
                                strValue = "five_zero_two_(33.0 / 4.0)";
                            } else if (Width > 29) {
                                if (BB_Area <= 900) {
                                    strValue = "five_one_one_(5.0 / 1.0)";
                                } else if (BB_Area > 900) {
                                    strValue = "five_zero_two_(2.0)";
                                }
                            }
                        } else if (Black_Pixels_Count > 246) {
                            if (Height <= 31) {
                                strValue = "five_one_one_(35.0 / 8.0)";
                            } else if (Height > 31) {
                                strValue = "five_zero_six_(5.0)";
                            }
                        }
                    } else if (Width > 30) {
                        if (BB_Area <= 961) {
                            if (Black_Pixels_Count <= 275) {
                                if (BB_Area <= 930) {
                                    strValue = "five_one_five_(4.0 / 1.0)";
                                } else if (BB_Area > 930) {
                                    strValue = "five_zero_six_(9.0 / 1.0)";
                                }
                            } else if (Black_Pixels_Count > 275) {
                                strValue = "five_one_five_(18.0)";
                            }
                        } else if (BB_Area > 961) {
                            strValue = "five_zero_six_(13.0)";
                        }
                    }
                } else if (Width > 31) {
                    if (Black_Pixels_Count <= 310) {
                        if (Width <= 32) {
                            if (Black_Pixels_Count <= 291) {
                                strValue = "five_one_five_(13.0 / 2.0)";
                            } else if (Black_Pixels_Count > 291) {
                                strValue = "five_zero_five_(7.0 / 2.0)";
                            }
                        } else if (Width > 32) {
                            strValue = "five_zero_five_(28.0)";
                        }
                    } else if (Black_Pixels_Count > 310) {
                        strValue = "five_zero_one_(35.0)";
                    }
                }
            } else if (Width > 33) {
                if (Height <= 37) {
                    strValue = "five_zero_zero_(35.0)";
                } else if (Height > 37) {
                    if (Width <= 36) {
                        strValue = "five_zero_eight_(35.0)";
                    } else if (Width > 36) {
                        strValue = "five_zero_seven_(35.0)";
                    }
                }
            }
        }
        return strValue;
    }
}
