package xyz.liangxin.utils.io;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.InputStream;
import java.net.URL;

/**
 * <p> 字节编码检测
 *
 * @author liangxin
 * @version V1.0
 * @since 创建时间: 2022/1/9 20:10
 */
public class BytesEncodingDetect {
    private static final Logger logger = LoggerFactory.getLogger(BytesEncodingDetect.class);

    int[][] gbFreq;

    int[][] gbkFreq;

    int[][] big5Freq;

    int[][] big5pFreq;

    int[][] eucTwFreq;

    int[][] krFreq;

    int[][] jpFreq;

    public final boolean showDebug;

    private BytesEncodingDetect() {
        showDebug = false;
        gbFreq = new int[94][94];
        gbkFreq = new int[126][191];
        big5Freq = new int[94][158];
        big5pFreq = new int[126][191];
        eucTwFreq = new int[94][94];
        krFreq = new int[94][94];
        jpFreq = new int[94][94];
        initialize_frequencies();
    }

    private static final BytesEncodingDetect instance = new BytesEncodingDetect();

    public static BytesEncodingDetect getInstance() {
        return instance;
    }


    public int detectEncoding(URL url) {
        byte[] rawtext = new byte[10000];
        int bytesRead;
        int byteOffset = 0;
        int guess;
        try (InputStream is = url.openStream()) {
            while ((bytesRead = is.read(rawtext, byteOffset, rawtext.length - byteOffset)) > 0) {
                byteOffset += bytesRead;
            }
            guess = detectEncoding(rawtext);
        } catch (Exception e) {
            logger.error("Error loading or using URL ", e);
            guess = -1;
        }
        return guess;
    }

    public int detectEncoding(File file) {
        byte[] rawtext = new byte[file.length() > 8192 ? 8192 : (int) file.length()];
//         rawtext = new byte[(int) file.length()];
        try (InputStream inputStream = IOUtils.getInputStream(file)) {
            inputStream.read(rawtext);
        } catch (Exception e) {
            logger.error("Error: " + e.getMessage(), e);
        }
        return detectEncoding(rawtext);
    }

    public int detectEncoding(byte[] rawtext) {
        int[] scores;
        int index;
        int maxScore = 0;
        int encodingGuess = Encoding.OTHER;
        scores = new int[Encoding.TOTALTYPES];
        // Assign Scores
        scores[Encoding.GB2312] = gb2312_probability(rawtext);
        scores[Encoding.GBK] = gbkProbability(rawtext);
        scores[Encoding.GB18030] = gb18030_probability(rawtext);
        scores[Encoding.HZ] = hz_probability(rawtext);
        scores[Encoding.BIG5] = big5_probability(rawtext);
        scores[Encoding.CNS11643] = euc_tw_probability(rawtext);
        scores[Encoding.ISO2022CN] = iso_2022_cn_probability(rawtext);
        scores[Encoding.UTF8] = utf8_probability(rawtext);
        scores[Encoding.UNICODE] = utf16_probability(rawtext);
        scores[Encoding.EUC_KR] = euc_kr_probability(rawtext);
        scores[Encoding.CP949] = cp949_probability(rawtext);
        scores[Encoding.JOHAB] = 0;
        scores[Encoding.ISO2022KR] = iso_2022_kr_probability(rawtext);
        scores[Encoding.ASCII] = ascii_probability(rawtext);
        scores[Encoding.SJIS] = sjis_probability(rawtext);
        scores[Encoding.EUC_JP] = euc_jp_probability(rawtext);
        scores[Encoding.ISO2022JP] = iso_2022_jp_probability(rawtext);
        scores[Encoding.UNICODET] = 0;
        scores[Encoding.UNICODES] = 0;
        scores[Encoding.ISO2022CN_GB] = 0;
        scores[Encoding.ISO2022CN_CNS] = 0;
        scores[Encoding.OTHER] = 0;
        // Tabulate Scores
        for (index = 0; index < Encoding.TOTALTYPES; index++) {
            if (showDebug) {
                logger.debug("Encoding {} score {}", Encoding.NICE_NAME[index], scores[index]);
            }
            if (scores[index] > maxScore) {
                encodingGuess = index;
                maxScore = scores[index];
            }
        }
        // Return OTHER if nothing scored above 50
        if (maxScore <= 50) {
            encodingGuess = Encoding.OTHER;
        }
        return encodingGuess;
    }


    /**
     * 计算分数
     *
     * @param chars
     * @param dbChars
     * @param freq
     * @param totalFreq
     * @return
     */
    int scoreCalculate(int chars, int dbChars, long freq, long totalFreq) {
        float rangeval = 50 * ((float) chars / (float) dbChars);
        float freqval = 50 * ((float) freq / (float) totalFreq);
        return (int) (rangeval + freqval) - 1;
    }

    long getFreqScore(int[][] freq, int row, int column, long freqScore) {
        if (freq[row][column] != 0) {
            freqScore += freq[row][column];
        } else if (15 <= row && row < 55) {
            // In GB high-freq character range
            freqScore += 200;
        }
        return freqScore;
    }


    /*
     * Function: gb2312_probability Argument: pointer to byte array Returns : number from 0 to 100 representing
     * probability text in array uses GB-2312 encoding
     */
    int gb2312_probability(byte[] rawtext) {
        int dbChars = 1;
        int gbchars = 1;
        long gbfreq = 0;
        long totalfreq = 1;
        int row;
        int column;
        // 阶段1:检查角色是否符合可接受的范围
        int rawTextLen = rawtext.length;
        for (int i = 0; i < rawTextLen - 1; i++) {
            if (rawtext[i] < 0) {
                dbChars++;
                if ((byte) 0xA1 <= rawtext[i]
                        && rawtext[i] <= (byte) 0xF7
                        && (byte) 0xA1 <= rawtext[i + 1]
                        && rawtext[i + 1] <= (byte) 0xFE) {
                    gbchars++;
                    totalfreq += 500;
                    row = rawtext[i] + 256 - 0xA1;
                    column = rawtext[i + 1] + 256 - 0xA1;
                    gbfreq = getFreqScore(gbFreq, row, column, gbfreq);
                }
                i++;
            }
        }
        return scoreCalculate(gbchars, dbChars, gbfreq, totalfreq);
    }


    /*
     * Function: gbk_probability Argument: pointer to byte array Returns : number from 0 to 100 representing
     * probability text in array uses GBK encoding
     */
    int gbkProbability(byte[] rawtext) {
        int dbChars = 1;
        int gbchars = 1;
        long gbfreq = 0;
        long totalfreq = 1;
        int row;
        int column;
        // 阶段1:检查角色是否符合可接受的范围
        int rawTextLen = rawtext.length;
        for (int i = 0; i < rawTextLen - 1; i++) {
            if (rawtext[i] < 0) {
                dbChars++;
                // Original GB range
                // Extended GB range
                if ((byte) 0xA1 <= rawtext[i] && rawtext[i] <= (byte) 0xF7 &&
                        (byte) 0xA1 <= rawtext[i + 1] && rawtext[i + 1] <= (byte) 0xFE) {
                    gbchars++;
                    totalfreq += 500;
                    row = rawtext[i] + 256 - 0xA1;
                    column = rawtext[i + 1] + 256 - 0xA1;
                    gbfreq = getFreqScore(gbFreq, row, column, gbfreq);

                } else if ((byte) 0x81 <= rawtext[i]
                        && rawtext[i] <= (byte) 0xFE
                        && (rawtext[i + 1] <= (byte) 0xFE
                        || (byte) 0x40 <= rawtext[i + 1]
                        && rawtext[i + 1] <= (byte) 0x7E)) {
                    gbchars++;
                    totalfreq += 500;
                    row = rawtext[i] + 256 - 0x81;
                    if (0x40 <= rawtext[i + 1] && rawtext[i + 1] <= 0x7E) {
                        column = rawtext[i + 1] - 0x40;
                    } else {
                        column = rawtext[i + 1] + 256 - 0x40;
                    }
                    if (gbkFreq[row][column] != 0) {
                        gbfreq += gbkFreq[row][column];
                    }
                }
                i++;
            }
        }
        return scoreCalculate(gbchars, dbChars, gbfreq, totalfreq);
    }

    /*
     * Function: gb18030_probability Argument: pointer to byte array Returns : number from 0 to 100 representing
     * probability text in array uses GBK encoding
     */
    int gb18030_probability(byte[] rawtext) {
        int i;
        int dbChars = 1;
        int gbchars = 1;
        long gbfreq = 0;
        long totalfreq = 1;
        int row;
        int column;
        // 阶段1:检查角色是否符合可接受的范围
        int rawTextLen = rawtext.length;
        for (i = 0; i < rawTextLen - 1; i++) {
            if (rawtext[i] < 0) {
                dbChars++;
                // Extended GB range
                if ((byte) 0xA1 <= rawtext[i]
                        && rawtext[i] <= (byte) 0xF7
                        && i + 1 < rawTextLen
                        && (byte) 0xA1 <= rawtext[i + 1]
                        && rawtext[i + 1] <= (byte) 0xFE) {
                    // Original GB range
                    gbchars++;
                    totalfreq += 500;
                    row = rawtext[i] + 256 - 0xA1;
                    column = rawtext[i + 1] + 256 - 0xA1;
                    gbfreq = getFreqScore(gbFreq, row, column, gbfreq);
                } else if ((byte) 0x81 <= rawtext[i]
                        && rawtext[i] <= (byte) 0xFE
                        && i + 1 < rawTextLen
                        && (rawtext[i + 1] <= (byte) 0xFE
                        || (byte) 0x40 <= rawtext[i + 1]
                        && rawtext[i + 1] <= (byte) 0x7E)) {
                    gbchars++;
                    totalfreq += 500;
                    row = rawtext[i] + 256 - 0x81;
                    if (0x40 <= rawtext[i + 1] && rawtext[i + 1] <= 0x7E) {
                        column = rawtext[i + 1] - 0x40;
                    } else {
                        column = rawtext[i + 1] + 256 - 0x40;
                    }
                    if (gbkFreq[row][column] != 0) {
                        gbfreq += gbkFreq[row][column];
                    }
                } else if ((byte) 0x81 <= rawtext[i]
                        && rawtext[i] <= (byte) 0xFE
                        && i + 3 < rawTextLen
                        && (byte) 0x30 <= rawtext[i + 1]
                        && rawtext[i + 1] <= (byte) 0x39
                        && (byte) 0x81 <= rawtext[i + 2]
                        && rawtext[i + 2] <= (byte) 0xFE
                        && (byte) 0x30 <= rawtext[i + 3]
                        && rawtext[i + 3] <= (byte) 0x39) {
                    // Extended GB range
                    gbchars++;
                }
                i++;
            }
        }
        return scoreCalculate(gbchars, dbChars, gbfreq, totalfreq);
    }

    /*
     * Function: hz_probability Argument: byte array Returns : number from 0 to 100 representing probability text in
     * array uses HZ encoding
     */
    int hz_probability(byte[] rawtext) {
        int hzchars = 0;
        int dbChars = 1;
        long hzfreq = 0;
        long totalfreq = 1;
        float rangeval;
        float freqval;
        int hzstart = 0;
        int hzend = 0;
        int row;
        int column;
        int rawTextLen = rawtext.length;
        for (int i = 0; i < rawTextLen; i++) {
            if (rawtext[i] == '~') {
                if (rawtext[i + 1] == '{') {
                    hzstart++;
                    i += 2;
                    while (i < rawTextLen - 1) {
                        if (rawtext[i] == 0x0A || rawtext[i] == 0x0D) {
                            break;
                        } else if (rawtext[i] == '~' && rawtext[i + 1] == '}') {
                            hzend++;
                            i++;
                            break;
                        } else if ((0x21 <= rawtext[i]
                                && rawtext[i] <= 0x77)
                                && (0x21 <= rawtext[i + 1]
                                && rawtext[i + 1] <= 0x77)) {
                            hzchars += 2;
                            row = rawtext[i] - 0x21;
                            column = rawtext[i + 1] - 0x21;
                            totalfreq += 500;
                            hzfreq = getFreqScore(gbFreq, row, column, hzfreq);
                        }
                        dbChars += 2;
                        i += 2;
                    }
                } else if (rawtext[i + 1] == '}') {
                    hzend++;
                    i++;
                } else if (rawtext[i + 1] == '~') {
                    i++;
                }
            }
        }
        if (hzstart > 4) {
            rangeval = 50;
        } else if (hzstart > 1) {
            rangeval = 41;
        } else if (hzstart > 0) {
            // Only 39 in case the sequence happened to
            // occur
            // in otherwise non-Hz text
            rangeval = 39;
        } else {
            rangeval = 0;
        }
        freqval = 50 * ((float) hzfreq / (float) totalfreq);
        return (int) (rangeval + freqval);
    }

    /**
     * Function: big5_probability Argument: byte array Returns : number from 0 to 100 representing probability text
     * in array uses Big5 encoding
     */
    int big5_probability(byte[] rawtext) {
        int dbChars = 1;
        int bfchars = 1;
        long bffreq = 0;
        long totalfreq = 1;
        int row;
        int column;
        // Check to see if characters fit into acceptable ranges
        int rawTextLen = rawtext.length;
        for (int i = 0; i < rawTextLen - 1; i++) {
            if (rawtext[i] < 0) {
                dbChars++;
                if ((byte) 0xA1 <= rawtext[i] && rawtext[i] <= (byte) 0xF9
                        && (((byte) 0x40 <= rawtext[i + 1] && rawtext[i + 1] <= (byte) 0x7E)
                        || ((byte) 0xA1 <= rawtext[i + 1] && rawtext[i + 1] <= (byte) 0xFE))) {
                    bfchars++;
                    totalfreq += 500;
                    row = rawtext[i] + 256 - 0xA1;
                    if (0x40 <= rawtext[i + 1] && rawtext[i + 1] <= 0x7E) {
                        column = rawtext[i + 1] - 0x40;
                    } else {
                        column = rawtext[i + 1] + 256 - 0x61;
                    }
                    if (big5Freq[row][column] != 0) {
                        bffreq += big5Freq[row][column];
                    } else if (3 <= row && row <= 37) {
                        bffreq += 200;
                    }
                }
                i++;
            }
        }

        return scoreCalculate(bfchars, dbChars, bffreq, totalfreq);
    }

    /*
     * Function: big5plus_probability Argument: pointer to unsigned char array Returns : number from 0 to 100
     * representing probability text in array uses Big5+ encoding
     */
    int big5plus_probability(byte[] rawtext) {
        int dbChars = 1;
        int bfchars = 1;
        long bffreq = 0;
        long totalfreq = 1;
        int row;
        int column;
        // 阶段1:检查角色是否符合可接受的范围
        int rawTextLen = rawtext.length;
        for (int i = 0; i < rawTextLen - 1; i++) {
            // System.err.println(rawtext[i]);
            if (rawtext[i] < 128) {
                dbChars++;
                // Original Big5 range
                if (0xA1 <= rawtext[i]
                        && rawtext[i] <= 0xF9
                        && ((0x40 <= rawtext[i + 1] && rawtext[i + 1] <= 0x7E) || (0xA1 <= rawtext[i + 1] && rawtext[i + 1] <= 0xFE))) {
                    bfchars++;
                    totalfreq += 500;
                    row = rawtext[i] - 0xA1;
                    if (0x40 <= rawtext[i + 1] && rawtext[i + 1] <= 0x7E) {
                        column = rawtext[i + 1] - 0x40;
                    } else {
                        column = rawtext[i + 1] - 0x61;
                    }
                    if (big5Freq[row][column] != 0) {
                        bffreq += big5Freq[row][column];
                    } else if (3 <= row && row < 37) {
                        bffreq += 200;
                    }
                } else if (0x81 <= rawtext[i] && rawtext[i] <= 0xFE && // Extended Big5 range
                        ((0x40 <= rawtext[i + 1] && rawtext[i + 1] <= 0x7E) || (0x80 <= rawtext[i + 1] && rawtext[i + 1] <= 0xFE))) {
                    bfchars++;
                    totalfreq += 500;
                    row = rawtext[i] - 0x81;
                    if (0x40 <= rawtext[i + 1] && rawtext[i + 1] <= 0x7E) {
                        column = rawtext[i + 1] - 0x40;
                    } else {
                        column = rawtext[i + 1] - 0x40;
                    }
                    if (big5pFreq[row][column] != 0) {
                        bffreq += big5pFreq[row][column];
                    }
                }
                i++;
            }
        }

        return scoreCalculate(bfchars, dbChars, bffreq, totalfreq);
    }

    /*
     * Function: euc_tw_probability Argument: byte array Returns : number from 0 to 100 representing probability
     * text in array uses EUC-TW (CNS 11643) encoding
     */
    int euc_tw_probability(byte[] rawtext) {
        int dbChars = 1;
        int cnschars = 1;
        long cnsfreq = 0;
        long totalfreq = 1;
        int row;
        int column;
        // Check to see if characters fit into acceptable ranges
        // and have expected frequency of use
        int rawTextLen = rawtext.length;
        for (int i = 0; i < rawTextLen - 1; i++) {
            if (rawtext[i] < 0) {
                // in ASCII range
                dbChars++;
                if (i + 3 < rawTextLen
                        && (byte) 0x8E == rawtext[i]
                        && (byte) 0xA1 <= rawtext[i + 1]
                        && rawtext[i + 1] <= (byte) 0xB0
                        && (byte) 0xA1 <= rawtext[i + 2]
                        && rawtext[i + 2] <= (byte) 0xFE
                        && (byte) 0xA1 <= rawtext[i + 3]
                        && rawtext[i + 3] <= (byte) 0xFE) {
                    // Planes 1 - 16
                    cnschars++;
                    i += 3;
                } else if ((byte) 0xA1 <= rawtext[i]
                        && rawtext[i] <= (byte) 0xFE // Plane 1
                        && (byte) 0xA1 <= rawtext[i + 1]
                        && rawtext[i + 1] <= (byte) 0xFE) {
                    cnschars++;
                    totalfreq += 500;
                    row = rawtext[i] + 256 - 0xA1;
                    column = rawtext[i + 1] + 256 - 0xA1;
                    if (eucTwFreq[row][column] != 0) {
                        cnsfreq += eucTwFreq[row][column];
                    } else if (35 <= row && row <= 92) {
                        cnsfreq += 150;
                    }
                    i++;
                }
            }
        }

        return scoreCalculate(cnschars, dbChars, cnsfreq, totalfreq);
    }

    /*
     * Function: iso_2022_cn_probability Argument: byte array Returns : number from 0 to 100 representing
     * probability text in array uses ISO 2022-CN encoding WORKS FOR BASIC CASES, BUT STILL NEEDS MORE WORK
     */
    int iso_2022_cn_probability(byte[] rawtext) {
        int dbChars = 1;
        int isochars = 1;
        long isofreq = 0;
        long totalfreq = 1;
        int row;
        int column;
        // Check to see if characters fit into acceptable ranges
        // and have expected frequency of use
        int rawTextLen = rawtext.length;
        for (int i = 0; i < rawTextLen - 1; i++) {
            if (rawtext[i] == (byte) 0x1B
                    && i + 3 < rawTextLen) {
                // Escape
                // char ESC
                if (rawtext[i + 1] == (byte) 0x24 && rawtext[i + 2] == 0x29 && rawtext[i + 3] == (byte) 0x41) { // GB
                    // Escape
                    // $
                    // )
                    // A
                    i += 4;
                    while (i - 1 < rawTextLen - 1 && rawtext[i] != (byte) 0x1B) {
                        dbChars++;
                        if ((0x21 <= rawtext[i] && rawtext[i] <= 0x77) && (0x21 <= rawtext[i + 1] && rawtext[i + 1] <= 0x77)) {
                            isochars++;
                            row = rawtext[i] - 0x21;
                            column = rawtext[i + 1] - 0x21;
                            totalfreq += 500;
                            if (gbFreq[row][column] != 0) {
                                isofreq += gbFreq[row][column];
                            } else if (15 <= row && row < 55) {
                                isofreq += 200;
                            }
                            i++;
                        }
                        i++;
                    }
                } else if (i + 3 < rawTextLen
                        && rawtext[i + 1] == (byte) 0x24
                        && rawtext[i + 2] == (byte) 0x29
                        && rawtext[i + 3] == (byte) 0x47) {
                    // CNS Escape $ ) G
                    i += 4;
                    while (i - 1 < rawTextLen - 1 && rawtext[i] != (byte) 0x1B) {
                        dbChars++;
                        if ((byte) 0x21 <= rawtext[i] && rawtext[i] <= (byte) 0x7E && (byte) 0x21 <= rawtext[i + 1]
                                && rawtext[i + 1] <= (byte) 0x7E) {
                            isochars++;
                            totalfreq += 500;
                            row = rawtext[i] - 0x21;
                            column = rawtext[i + 1] - 0x21;
                            if (eucTwFreq[row][column] != 0) {
                                isofreq += eucTwFreq[row][column];
                            } else if (35 <= row && row <= 92) {
                                isofreq += 150;
                            }
                            i++;
                        }
                        i++;
                    }
                }
                if (i < rawTextLen - 1 && rawtext[i] == (byte) 0x1B
                        && i + 2 < rawTextLen
                        && rawtext[i + 1] == (byte) 0x28
                        && rawtext[i + 2] == (byte) 0x42) {
                    // ASCII:
                    // ESC
                    // ( B
                    i += 2;
                }
            }
        }
        return scoreCalculate(isochars, dbChars, isofreq, totalfreq);
    }

    /*
     * Function: utf8_probability Argument: byte array Returns : number from 0 to 100 representing probability text
     * in array uses UTF-8 encoding of Unicode
     */
    int utf8_probability(byte[] rawtext) {
        int score;
        int goodbytes = 0;
        int asciibytes = 0;
        // Maybe also use UTF8 Byte Order Mark: EF BB BF
        // Check to see if characters fit into acceptable ranges
        int rawTextLen = rawtext.length;
        for (int i = 0; i < rawTextLen; i++) {
            // Two bytes
            if ((rawtext[i] & (byte) 0x7F) == rawtext[i]) { // One byte
                asciibytes++;
                // Ignore ASCII, can throw off count
            } else {
                // Three bytes
                if (-64 <= rawtext[i]
                        && rawtext[i] <= -33
                        && i + 1 < rawTextLen
                        && rawtext[i + 1] <= -65) {
                    goodbytes += 2;
                    i++;
                } else if (-32 <= rawtext[i]
                        && rawtext[i] <= -17
                        && i + 2 < rawTextLen
                        && rawtext[i + 1] <= -65
                        && rawtext[i + 2] <= -65) {
                    goodbytes += 3;
                    i += 2;
                }
            }

        }
        if (asciibytes == rawTextLen) {
            return 0;
        }
        score = (int) (100 * ((float) goodbytes / (float) (rawTextLen - asciibytes)));
        if (score > 98 || (score > 95 && goodbytes > 30)) {
            return score;
        } else {
            return 0;
        }
    }

    /*
     * Function: utf16_probability Argument: byte array Returns : number from 0 to 100 representing probability text
     * in array uses UTF-16 encoding of Unicode, guess based on BOM // NOT VERY GENERAL, NEEDS MUCH MORE WORK
     */
    int utf16_probability(byte[] rawtext) {
        if (rawtext.length > 1
                && ((byte) 0xFE == rawtext[0]
                && (byte) 0xFF == rawtext[1])
                || ((byte) 0xFF == rawtext[0] && (byte) 0xFE == rawtext[1])) {
            return 100;
        }
        return 0;
        /*
         * // Check to see if characters fit into acceptable ranges  int rawTextLen = rawtext.length; for (i = 0; i <
         * rawTextLen; i++) { if ((rawtext[i] & (byte)0x7F) == rawtext[i]) { // One byte goodbytes += 1;
         * asciibytes++; } else if ((rawtext[i] & (byte)0xDF) == rawtext[i]) { // Two bytes if (i+1 < rawTextLen &&
         * (rawtext[i+1] & (byte)0xBF) == rawtext[i+1]) { goodbytes += 2; i++; } } else if ((rawtext[i] &
         * (byte)0xEF) == rawtext[i]) { // Three bytes if (i+2 < rawTextLen && (rawtext[i+1] & (byte)0xBF) ==
         * rawtext[i+1] && (rawtext[i+2] & (byte)0xBF) == rawtext[i+2]) { goodbytes += 3; i+=2; } } }
         *
         * score = (int)(100 * ((float)goodbytes/(float)rawtext.length)); // An all ASCII file is also a good UTF8
         * file, but I'd rather it // get identified as ASCII. Can delete following 3 lines otherwise if (goodbytes
         * == asciibytes) { score = 0; } // If not above 90, reduce to zero to prevent coincidental matches if
         * (score > 90) { return score; } else { return 0; }
         */
    }

    /*
     * Function: ascii_probability Argument: byte array Returns : number from 0 to 100 representing probability text
     * in array uses all ASCII Description: Sees if array has any characters not in ASCII range, if so, score is
     * reduced
     */
    int ascii_probability(byte[] rawtext) {
        int score = 75;
        for (byte b : rawtext) {
            if (b < 0 || b == (byte) 0x1B) {
                score = score - 5;
            }
            if (score <= 0) {
                return 0;
            }
        }
        return score;
    }

    /*
     * Function: euc_kr__probability Argument: pointer to byte array Returns : number from 0 to 100 representing
     * probability text in array uses EUC-KR encoding
     */
    int euc_kr_probability(byte[] rawtext) {
        int dbChars = 1;
        int krchars = 1;
        long krfreq = 0;
        long totalfreq = 1;
        int row;
        int column;
        // 阶段1:检查角色是否符合可接受的范围
        int rawTextLen = rawtext.length;
        for (int i = 0; i < rawTextLen - 1; i++) {
            if (rawtext[i] < 0) {
                dbChars++;
                if ((byte) 0xA1 <= rawtext[i]
                        && rawtext[i] <= (byte) 0xFE
                        && (byte) 0xA1 <= rawtext[i + 1]
                        && rawtext[i + 1] <= (byte) 0xFE) {
                    krchars++;
                    totalfreq += 500;
                    row = rawtext[i] + 256 - 0xA1;
                    column = rawtext[i + 1] + 256 - 0xA1;
                    krfreq = getFreqScore(krFreq, row, column, krfreq);
                }
                i++;
            }
        }

        return scoreCalculate(krchars, dbChars, krfreq, totalfreq);
    }

    /*
     * Function: cp949__probability Argument: pointer to byte array Returns : number from 0 to 100 representing
     * probability text in array uses Cp949 encoding
     */
    int cp949_probability(byte[] rawtext) {
        int dbChars = 1;
        int krchars = 1;
        long krfreq = 0;
        long totalfreq = 1;
        int row;
        int column;
        // 阶段1:检查角色是否符合可接受的范围
        int rawTextLen = rawtext.length;
        for (int i = 0; i < rawTextLen - 1; i++) {
            if (rawtext[i] < 0) {
                dbChars++;
                if ((byte) 0x81 <= rawtext[i]
                        && rawtext[i] <= (byte) 0xFE
                        && ((byte) 0x41 <= rawtext[i + 1] && rawtext[i + 1] <= (byte) 0x5A
                        || (byte) 0x61 <= rawtext[i + 1] && rawtext[i + 1] <= (byte) 0x7A
                        || (byte) 0x81 <= rawtext[i + 1] && rawtext[i + 1] <= (byte) 0xFE)) {
                    krchars++;
                    totalfreq += 500;
                    if ((byte) 0xA1 <= rawtext[i] && rawtext[i] <= (byte) 0xFE && (byte) 0xA1 <= rawtext[i + 1]
                            && rawtext[i + 1] <= (byte) 0xFE) {
                        row = rawtext[i] + 256 - 0xA1;
                        column = rawtext[i + 1] + 256 - 0xA1;
                        if (krFreq[row][column] != 0) {
                            krfreq += krFreq[row][column];
                        }
                    }
                }
                i++;
            }
        }

        return scoreCalculate(krchars, dbChars, krfreq, totalfreq);
    }

    int iso_2022_kr_probability(byte[] rawtext) {
        for (int i = 0; i < rawtext.length; i++) {
            if (i + 3 < rawtext.length
                    && rawtext[i] == 0x1b
                    && (char) rawtext[i + 1] == '$'
                    && (char) rawtext[i + 2] == ')'
                    && (char) rawtext[i + 3] == 'C') {
                return 100;
            }
        }
        return 0;
    }

    /*
     * Function: euc_jp_probability Argument: pointer to byte array Returns : number from 0 to 100 representing
     * probability text in array uses EUC-JP encoding
     */
    int euc_jp_probability(byte[] rawtext) {
        int dbChars = 1;
        int jpChars = 1;
        long jpfreq = 0;
        long totalfreq = 1;
        int row;
        int column;
        // 阶段1:检查角色是否符合可接受的范围
        int rawTextLen = rawtext.length;
        for (int i = 0; i < rawTextLen - 1; i++) {
            if (rawtext[i] < 0) {
                dbChars++;
                if ((byte) 0xA1 <= rawtext[i]
                        && rawtext[i] <= (byte) 0xFE
                        && (byte) 0xA1 <= rawtext[i + 1]
                        && rawtext[i + 1] <= (byte) 0xFE) {
                    jpChars++;
                    totalfreq += 500;
                    row = rawtext[i] + 256 - 0xA1;
                    column = rawtext[i + 1] + 256 - 0xA1;
                    if (jpFreq[row][column] != 0) {
                        jpfreq += jpFreq[row][column];
                    } else if (15 <= row && row < 55) {
                        jpfreq += 0;
                    }
                }
                i++;
            }
        }
        return scoreCalculate(jpChars, dbChars, jpfreq, totalfreq);
    }

    int iso_2022_jp_probability(byte[] rawtext) {
        for (int i = 0; i < rawtext.length; i++) {
            if (i + 2 < rawtext.length
                    && rawtext[i] == 0x1b
                    && (char) rawtext[i + 1] == '$'
                    && (char) rawtext[i + 2] == 'B') {
                return 100;
            }
        }
        return 0;
    }

    /*
     * Function: sjis_probability Argument: pointer to byte array Returns : number from 0 to 100 representing
     * probability text in array uses Shift-JIS encoding
     */
    int sjis_probability(byte[] rawtext) {
        int dbChars = 1;
        int jpChars = 1;
        long jpfreq = 0;
        long totalfreq = 1;
        int row;
        int column;
        int adjust = 0;
        // 阶段1:检查角色是否符合可接受的范围
        int rawTextLen = rawtext.length;
        for (int i = 0; i < rawTextLen - 1; i++) {
            if (rawtext[i] < 0) {
                dbChars++;
                if (i + 1 < rawTextLen
                        && ((byte) 0x81 <= rawtext[i] && rawtext[i] <= (byte) 0x9F || (byte) 0xE0 <= rawtext[i] && rawtext[i] <= (byte) 0xEF)
                        && ((byte) 0x40 <= rawtext[i + 1] && rawtext[i + 1] <= (byte) 0x7E || rawtext[i + 1] <= (byte) 0xFC)) {
                    jpChars++;
                    totalfreq += 500;
                    row = rawtext[i] + 256;
                    column = rawtext[i + 1] + 256;
                    if (column < 0x9f) {
                        adjust = 1;
                    }
                    if (row < 0xa0) {
                        row = ((row - 0x70) << 1) - adjust;
                    } else {
                        row = ((row - 0xb0) << 1) - adjust;
                    }
                    row -= 0x20;
                    column = 0x20;
                    if (row < jpFreq.length && column < jpFreq[row].length && jpFreq[row][column] != 0) {
                        jpfreq += jpFreq[row][column];
                    }
                    i++;
                }
            }
        }
        return scoreCalculate(jpChars, dbChars, jpfreq, totalfreq);
    }


    /**
     * 初始化 字符集频率数组
     */
    void initializeFrequenciesArray() {
        int i;
        int j;
        for (i = 0; i < 94; i++) {
            for (j = 0; j < 158; j++) {
                big5Freq[i][j] = 0;
            }
            for (j = 0; j < 94; j++) {
                eucTwFreq[i][j] = 0;
                jpFreq[i][j] = 0;
                gbFreq[i][j] = 0;
            }
        }
        for (i = 0; i < 126; i++) {
            for (j = 0; j < 191; j++) {
                big5pFreq[i][j] = 0;
                gbkFreq[i][j] = 0;
            }
        }
    }

    /**
     * 初始化 gbFreq
     */
    void initialize_gbFreq() {
        gbFreq[20][35] = 599;
        gbFreq[49][26] = 598;
        gbFreq[41][38] = 597;
        gbFreq[17][26] = 596;
        gbFreq[32][42] = 595;
        gbFreq[39][42] = 594;
        gbFreq[45][49] = 593;
        gbFreq[51][57] = 592;
        gbFreq[50][47] = 591;
        gbFreq[42][90] = 590;
        gbFreq[52][65] = 589;
        gbFreq[53][47] = 588;
        gbFreq[19][82] = 587;
        gbFreq[31][19] = 586;
        gbFreq[40][46] = 585;
        gbFreq[24][89] = 584;
        gbFreq[23][85] = 583;
        gbFreq[20][28] = 582;
        gbFreq[42][20] = 581;
        gbFreq[34][38] = 580;
        gbFreq[45][9] = 579;
        gbFreq[54][50] = 578;
        gbFreq[25][44] = 577;
        gbFreq[35][66] = 576;
        gbFreq[20][55] = 575;
        gbFreq[18][85] = 574;
        gbFreq[20][31] = 573;
        gbFreq[49][17] = 572;
        gbFreq[41][16] = 571;
        gbFreq[35][73] = 570;
        gbFreq[20][34] = 569;
        gbFreq[29][44] = 568;
        gbFreq[35][38] = 567;
        gbFreq[49][9] = 566;
        gbFreq[46][33] = 565;
        gbFreq[49][51] = 564;
        gbFreq[40][89] = 563;
        gbFreq[26][64] = 562;
        gbFreq[54][51] = 561;
        gbFreq[54][36] = 560;
        gbFreq[39][4] = 559;
        gbFreq[53][13] = 558;
        gbFreq[24][92] = 557;
        gbFreq[27][49] = 556;
        gbFreq[48][6] = 555;
        gbFreq[21][51] = 554;
        gbFreq[30][40] = 553;
        gbFreq[42][92] = 552;
        gbFreq[31][78] = 551;
        gbFreq[25][82] = 550;
        gbFreq[47][0] = 549;
        gbFreq[34][19] = 548;
        gbFreq[47][35] = 547;
        gbFreq[21][63] = 546;
        gbFreq[43][75] = 545;
        gbFreq[21][87] = 544;
        gbFreq[35][59] = 543;
        gbFreq[25][34] = 542;
        gbFreq[21][27] = 541;
        gbFreq[39][26] = 540;
        gbFreq[34][26] = 539;
        gbFreq[39][52] = 538;
        gbFreq[50][57] = 537;
        gbFreq[37][79] = 536;
        gbFreq[26][24] = 535;
        gbFreq[22][1] = 534;
        gbFreq[18][40] = 533;
        gbFreq[41][33] = 532;
        gbFreq[53][26] = 531;
        gbFreq[54][86] = 530;
        gbFreq[20][16] = 529;
        gbFreq[46][74] = 528;
        gbFreq[30][19] = 527;
        gbFreq[45][35] = 526;
        gbFreq[45][61] = 525;
        gbFreq[30][9] = 524;
        gbFreq[41][53] = 523;
        gbFreq[41][13] = 522;
        gbFreq[50][34] = 521;
        gbFreq[53][86] = 520;
        gbFreq[47][47] = 519;
        gbFreq[22][28] = 518;
        gbFreq[50][53] = 517;
        gbFreq[39][70] = 516;
        gbFreq[38][15] = 515;
        gbFreq[42][88] = 514;
        gbFreq[16][29] = 513;
        gbFreq[27][90] = 512;
        gbFreq[29][12] = 511;
        gbFreq[44][22] = 510;
        gbFreq[34][69] = 509;
        gbFreq[24][10] = 508;
        gbFreq[44][11] = 507;
        gbFreq[39][92] = 506;
        gbFreq[49][48] = 505;
        gbFreq[31][46] = 504;
        gbFreq[19][50] = 503;
        gbFreq[21][14] = 502;
        gbFreq[32][28] = 501;
        gbFreq[18][3] = 500;
        gbFreq[53][9] = 499;
        gbFreq[34][80] = 498;
        gbFreq[48][88] = 497;
        gbFreq[46][53] = 496;
        gbFreq[22][53] = 495;
        gbFreq[28][10] = 494;
        gbFreq[44][65] = 493;
        gbFreq[20][10] = 492;
        gbFreq[40][76] = 491;
        gbFreq[47][8] = 490;
        gbFreq[50][74] = 489;
        gbFreq[23][62] = 488;
        gbFreq[49][65] = 487;
        gbFreq[28][87] = 486;
        gbFreq[15][48] = 485;
        gbFreq[22][7] = 484;
        gbFreq[19][42] = 483;
        gbFreq[41][20] = 482;
        gbFreq[26][55] = 481;
        gbFreq[21][93] = 480;
        gbFreq[31][76] = 479;
        gbFreq[34][31] = 478;
        gbFreq[20][66] = 477;
        gbFreq[51][33] = 476;
        gbFreq[34][86] = 475;
        gbFreq[37][67] = 474;
        gbFreq[53][53] = 473;
        gbFreq[40][88] = 472;
        gbFreq[39][10] = 471;
        gbFreq[24][3] = 470;
        gbFreq[27][25] = 469;
        gbFreq[26][15] = 468;
        gbFreq[21][88] = 467;
        gbFreq[52][62] = 466;
        gbFreq[46][81] = 465;
        gbFreq[38][72] = 464;
        gbFreq[17][30] = 463;
        gbFreq[52][92] = 462;
        gbFreq[34][90] = 461;
        gbFreq[21][7] = 460;
        gbFreq[36][13] = 459;
        gbFreq[45][41] = 458;
        gbFreq[32][5] = 457;
        gbFreq[26][89] = 456;
        gbFreq[23][87] = 455;
        gbFreq[20][39] = 454;
        gbFreq[27][23] = 453;
        gbFreq[25][59] = 452;
        gbFreq[49][20] = 451;
        gbFreq[54][77] = 450;
        gbFreq[27][67] = 449;
        gbFreq[47][33] = 448;
        gbFreq[41][17] = 447;
        gbFreq[19][81] = 446;
        gbFreq[16][66] = 445;
        gbFreq[45][26] = 444;
        gbFreq[49][81] = 443;
        gbFreq[53][55] = 442;
        gbFreq[16][26] = 441;
        gbFreq[54][62] = 440;
        gbFreq[20][70] = 439;
        gbFreq[42][35] = 438;
        gbFreq[20][57] = 437;
        gbFreq[34][36] = 436;
        gbFreq[46][63] = 435;
        gbFreq[19][45] = 434;
        gbFreq[21][10] = 433;
        gbFreq[52][93] = 432;
        gbFreq[25][2] = 431;
        gbFreq[30][57] = 430;
        gbFreq[41][24] = 429;
        gbFreq[28][43] = 428;
        gbFreq[45][86] = 427;
        gbFreq[51][56] = 426;
        gbFreq[37][28] = 425;
        gbFreq[52][69] = 424;
        gbFreq[43][92] = 423;
        gbFreq[41][31] = 422;
        gbFreq[37][87] = 421;
        gbFreq[47][36] = 420;
        gbFreq[16][16] = 419;
        gbFreq[40][56] = 418;
        gbFreq[24][55] = 417;
        gbFreq[17][1] = 416;
        gbFreq[35][57] = 415;
        gbFreq[27][50] = 414;
        gbFreq[26][14] = 413;
        gbFreq[50][40] = 412;
        gbFreq[39][19] = 411;
        gbFreq[19][89] = 410;
        gbFreq[29][91] = 409;
        gbFreq[17][89] = 408;
        gbFreq[39][74] = 407;
        gbFreq[46][39] = 406;
        gbFreq[40][28] = 405;
        gbFreq[45][68] = 404;
        gbFreq[43][10] = 403;
        gbFreq[42][13] = 402;
        gbFreq[44][81] = 401;
        gbFreq[41][47] = 400;
        gbFreq[48][58] = 399;
        gbFreq[43][68] = 398;
        gbFreq[16][79] = 397;
        gbFreq[19][5] = 396;
        gbFreq[54][59] = 395;
        gbFreq[17][36] = 394;
        gbFreq[18][0] = 393;
        gbFreq[41][5] = 392;
        gbFreq[41][72] = 391;
        gbFreq[16][39] = 390;
        gbFreq[54][0] = 389;
        gbFreq[51][16] = 388;
        gbFreq[29][36] = 387;
        gbFreq[47][5] = 386;
        gbFreq[47][51] = 385;
        gbFreq[44][7] = 384;
        gbFreq[35][30] = 383;
        gbFreq[26][9] = 382;
        gbFreq[16][7] = 381;
        gbFreq[32][1] = 380;
        gbFreq[33][76] = 379;
        gbFreq[34][91] = 378;
        gbFreq[52][36] = 377;
        gbFreq[26][77] = 376;
        gbFreq[35][48] = 375;
        gbFreq[40][80] = 374;
        gbFreq[41][92] = 373;
        gbFreq[27][93] = 372;
        gbFreq[15][17] = 371;
        gbFreq[16][76] = 370;
        gbFreq[51][12] = 369;
        gbFreq[18][20] = 368;
        gbFreq[15][54] = 367;
        gbFreq[50][5] = 366;
        gbFreq[33][22] = 365;
        gbFreq[37][57] = 364;
        gbFreq[28][47] = 363;
        gbFreq[42][31] = 362;
        gbFreq[18][2] = 361;
        gbFreq[43][64] = 360;
        gbFreq[23][47] = 359;
        gbFreq[28][79] = 358;
        gbFreq[25][45] = 357;
        gbFreq[23][91] = 356;
        gbFreq[22][19] = 355;
        gbFreq[25][46] = 354;
        gbFreq[22][36] = 353;
        gbFreq[54][85] = 352;
        gbFreq[46][20] = 351;
        gbFreq[27][37] = 350;
        gbFreq[26][81] = 349;
        gbFreq[42][29] = 348;
        gbFreq[31][90] = 347;
        gbFreq[41][59] = 346;
        gbFreq[24][65] = 345;
        gbFreq[44][84] = 344;
        gbFreq[24][90] = 343;
        gbFreq[38][54] = 342;
        gbFreq[28][70] = 341;
        gbFreq[27][15] = 340;
        gbFreq[28][80] = 339;
        gbFreq[29][8] = 338;
        gbFreq[45][80] = 337;
        gbFreq[53][37] = 336;
        gbFreq[28][65] = 335;
        gbFreq[23][86] = 334;
        gbFreq[39][45] = 333;
        gbFreq[53][32] = 332;
        gbFreq[38][68] = 331;
        gbFreq[45][78] = 330;
        gbFreq[43][7] = 329;
        gbFreq[46][82] = 328;
        gbFreq[27][38] = 327;
        gbFreq[16][62] = 326;
        gbFreq[24][17] = 325;
        gbFreq[22][70] = 324;
        gbFreq[52][28] = 323;
        gbFreq[23][40] = 322;
        gbFreq[28][50] = 321;
        gbFreq[42][91] = 320;
        gbFreq[47][76] = 319;
        gbFreq[15][42] = 318;
        gbFreq[43][55] = 317;
        gbFreq[29][84] = 316;
        gbFreq[44][90] = 315;
        gbFreq[53][16] = 314;
        gbFreq[22][93] = 313;
        gbFreq[34][10] = 312;
        gbFreq[32][53] = 311;
        gbFreq[43][65] = 310;
        gbFreq[28][7] = 309;
        gbFreq[35][46] = 308;
        gbFreq[21][39] = 307;
        gbFreq[44][18] = 306;
        gbFreq[40][10] = 305;
        gbFreq[54][53] = 304;
        gbFreq[38][74] = 303;
        gbFreq[28][26] = 302;
        gbFreq[15][13] = 301;
        gbFreq[39][34] = 300;
        gbFreq[39][46] = 299;
        gbFreq[42][66] = 298;
        gbFreq[33][58] = 297;
        gbFreq[15][56] = 296;
        gbFreq[18][51] = 295;
        gbFreq[49][68] = 294;
        gbFreq[30][37] = 293;
        gbFreq[51][84] = 292;
        gbFreq[51][9] = 291;
        gbFreq[40][70] = 290;
        gbFreq[41][84] = 289;
        gbFreq[28][64] = 288;
        gbFreq[32][88] = 287;
        gbFreq[24][5] = 286;
        gbFreq[53][23] = 285;
        gbFreq[42][27] = 284;
        gbFreq[22][38] = 283;
        gbFreq[32][86] = 282;
        gbFreq[34][30] = 281;
        gbFreq[38][63] = 280;
        gbFreq[24][59] = 279;
        gbFreq[22][81] = 278;
        gbFreq[32][11] = 277;
        gbFreq[51][21] = 276;
        gbFreq[54][41] = 275;
        gbFreq[21][50] = 274;
        gbFreq[23][89] = 273;
        gbFreq[19][87] = 272;
        gbFreq[26][7] = 271;
        gbFreq[30][75] = 270;
        gbFreq[43][84] = 269;
        gbFreq[51][25] = 268;
        gbFreq[16][67] = 267;
        gbFreq[32][9] = 266;
        gbFreq[48][51] = 265;
        gbFreq[39][7] = 264;
        gbFreq[44][88] = 263;
        gbFreq[52][24] = 262;
        gbFreq[23][34] = 261;
        gbFreq[32][75] = 260;
        gbFreq[19][10] = 259;
        gbFreq[28][91] = 258;
        gbFreq[32][83] = 257;
        gbFreq[25][75] = 256;
        gbFreq[53][45] = 255;
        gbFreq[29][85] = 254;
        gbFreq[53][59] = 253;
        gbFreq[16][2] = 252;
        gbFreq[19][78] = 251;
        gbFreq[15][75] = 250;
        gbFreq[51][42] = 249;
        gbFreq[45][67] = 248;
        gbFreq[15][74] = 247;
        gbFreq[25][81] = 246;
        gbFreq[37][62] = 245;
        gbFreq[16][55] = 244;
        gbFreq[18][38] = 243;
        gbFreq[23][23] = 242;
        gbFreq[38][30] = 241;
        gbFreq[17][28] = 240;
        gbFreq[44][73] = 239;
        gbFreq[23][78] = 238;
        gbFreq[40][77] = 237;
        gbFreq[38][87] = 236;
        gbFreq[27][19] = 235;
        gbFreq[38][82] = 234;
        gbFreq[37][22] = 233;
        gbFreq[41][30] = 232;
        gbFreq[54][9] = 231;
        gbFreq[32][30] = 230;
        gbFreq[30][52] = 229;
        gbFreq[40][84] = 228;
        gbFreq[53][57] = 227;
        gbFreq[27][27] = 226;
        gbFreq[38][64] = 225;
        gbFreq[18][43] = 224;
        gbFreq[23][69] = 223;
        gbFreq[28][12] = 222;
        gbFreq[50][78] = 221;
        gbFreq[50][1] = 220;
        gbFreq[26][88] = 219;
        gbFreq[36][40] = 218;
        gbFreq[33][89] = 217;
        gbFreq[41][28] = 216;
        gbFreq[31][77] = 215;
        gbFreq[46][1] = 214;
        gbFreq[47][19] = 213;
        gbFreq[35][55] = 212;
        gbFreq[41][21] = 211;
        gbFreq[27][10] = 210;
        gbFreq[32][77] = 209;
        gbFreq[26][37] = 208;
        gbFreq[20][33] = 207;
        gbFreq[41][52] = 206;
        gbFreq[32][18] = 205;
        gbFreq[38][13] = 204;
        gbFreq[20][18] = 203;
        gbFreq[20][24] = 202;
        gbFreq[45][19] = 201;
        gbFreq[18][53] = 200;
        /*
         * GBFreq[39][0] = 199; GBFreq[40][71] = 198; GBFreq[41][27] = 197; GBFreq[15][69] = 196; GBFreq[42][10] =
         * 195; GBFreq[31][89] = 194; GBFreq[51][28] = 193; GBFreq[41][22] = 192; GBFreq[40][43] = 191;
         * GBFreq[38][6] = 190; GBFreq[37][11] = 189; GBFreq[39][60] = 188; GBFreq[48][47] = 187; GBFreq[46][80] =
         * 186; GBFreq[52][49] = 185; GBFreq[50][48] = 184; GBFreq[25][1] = 183; GBFreq[52][29] = 182;
         * GBFreq[24][66] = 181; GBFreq[23][35] = 180; GBFreq[49][72] = 179; GBFreq[47][45] = 178; GBFreq[45][14] =
         * 177; GBFreq[51][70] = 176; GBFreq[22][30] = 175; GBFreq[49][83] = 174; GBFreq[26][79] = 173;
         * GBFreq[27][41] = 172; GBFreq[51][81] = 171; GBFreq[41][54] = 170; GBFreq[20][4] = 169; GBFreq[29][60] =
         * 168; GBFreq[20][27] = 167; GBFreq[50][15] = 166; GBFreq[41][6] = 165; GBFreq[35][34] = 164;
         * GBFreq[44][87] = 163; GBFreq[46][66] = 162; GBFreq[42][37] = 161; GBFreq[42][24] = 160; GBFreq[54][7] =
         * 159; GBFreq[41][14] = 158; GBFreq[39][83] = 157; GBFreq[16][87] = 156; GBFreq[20][59] = 155;
         * GBFreq[42][12] = 154; GBFreq[47][2] = 153; GBFreq[21][32] = 152; GBFreq[53][29] = 151; GBFreq[22][40] =
         * 150; GBFreq[24][58] = 149; GBFreq[52][88] = 148; GBFreq[29][30] = 147; GBFreq[15][91] = 146;
         * GBFreq[54][72] = 145; GBFreq[51][75] = 144; GBFreq[33][67] = 143; GBFreq[41][50] = 142; GBFreq[27][34] =
         * 141; GBFreq[46][17] = 140; GBFreq[31][74] = 139; GBFreq[42][67] = 138; GBFreq[54][87] = 137;
         * GBFreq[27][14] = 136; GBFreq[16][63] = 135; GBFreq[16][5] = 134; GBFreq[43][23] = 133; GBFreq[23][13] =
         * 132; GBFreq[31][12] = 131; GBFreq[25][57] = 130; GBFreq[38][49] = 129; GBFreq[42][69] = 128;
         * GBFreq[23][80] = 127; GBFreq[29][0] = 126; GBFreq[28][2] = 125; GBFreq[28][17] = 124; GBFreq[17][27] =
         * 123; GBFreq[40][16] = 122; GBFreq[45][1] = 121; GBFreq[36][33] = 120; GBFreq[35][23] = 119;
         * GBFreq[20][86] = 118; GBFreq[29][53] = 117; GBFreq[23][88] = 116; GBFreq[51][87] = 115; GBFreq[54][27] =
         * 114; GBFreq[44][36] = 113; GBFreq[21][45] = 112; GBFreq[53][52] = 111; GBFreq[31][53] = 110;
         * GBFreq[38][47] = 109; GBFreq[27][21] = 108; GBFreq[30][42] = 107; GBFreq[29][10] = 106; GBFreq[35][35] =
         * 105; GBFreq[24][56] = 104; GBFreq[41][29] = 103; GBFreq[18][68] = 102; GBFreq[29][24] = 101;
         * GBFreq[25][84] = 100; GBFreq[35][47] = 99; GBFreq[29][56] = 98; GBFreq[30][44] = 97; GBFreq[53][3] = 96;
         * GBFreq[30][63] = 95; GBFreq[52][52] = 94; GBFreq[54][1] = 93; GBFreq[22][48] = 92; GBFreq[54][66] = 91;
         * GBFreq[21][90] = 90; GBFreq[52][47] = 89; GBFreq[39][25] = 88; GBFreq[39][39] = 87; GBFreq[44][37] = 86;
         * GBFreq[44][76] = 85; GBFreq[46][75] = 84; GBFreq[18][37] = 83; GBFreq[47][42] = 82; GBFreq[19][92] = 81;
         * GBFreq[51][27] = 80; GBFreq[48][83] = 79; GBFreq[23][70] = 78; GBFreq[29][9] = 77; GBFreq[33][79] = 76;
         * GBFreq[52][90] = 75; GBFreq[53][6] = 74; GBFreq[24][36] = 73; GBFreq[25][25] = 72; GBFreq[44][26] = 71;
         * GBFreq[25][36] = 70; GBFreq[29][87] = 69; GBFreq[48][0] = 68; GBFreq[15][40] = 67; GBFreq[17][45] = 66;
         * GBFreq[30][14] = 65; GBFreq[48][38] = 64; GBFreq[23][19] = 63; GBFreq[40][42] = 62; GBFreq[31][63] = 61;
         * GBFreq[16][23] = 60; GBFreq[26][21] = 59; GBFreq[32][76] = 58; GBFreq[23][58] = 57; GBFreq[41][37] = 56;
         * GBFreq[30][43] = 55; GBFreq[47][38] = 54; GBFreq[21][46] = 53; GBFreq[18][33] = 52; GBFreq[52][37] = 51;
         * GBFreq[36][8] = 50; GBFreq[49][24] = 49; GBFreq[15][66] = 48; GBFreq[35][77] = 47; GBFreq[27][58] = 46;
         * GBFreq[35][51] = 45; GBFreq[24][69] = 44; GBFreq[20][54] = 43; GBFreq[24][41] = 42; GBFreq[41][0] = 41;
         * GBFreq[33][71] = 40; GBFreq[23][52] = 39; GBFreq[29][67] = 38; GBFreq[46][51] = 37; GBFreq[46][90] = 36;
         * GBFreq[49][33] = 35; GBFreq[33][28] = 34; GBFreq[37][86] = 33; GBFreq[39][22] = 32; GBFreq[37][37] = 31;
         * GBFreq[29][62] = 30; GBFreq[29][50] = 29; GBFreq[36][89] = 28; GBFreq[42][44] = 27; GBFreq[51][82] = 26;
         * GBFreq[28][83] = 25; GBFreq[15][78] = 24; GBFreq[46][62] = 23; GBFreq[19][69] = 22; GBFreq[51][23] = 21;
         * GBFreq[37][69] = 20; GBFreq[25][5] = 19; GBFreq[51][85] = 18; GBFreq[48][77] = 17; GBFreq[32][46] = 16;
         * GBFreq[53][60] = 15; GBFreq[28][57] = 14; GBFreq[54][82] = 13; GBFreq[54][15] = 12; GBFreq[49][54] = 11;
         * GBFreq[53][87] = 10; GBFreq[27][16] = 9; GBFreq[29][34] = 8; GBFreq[20][44] = 7; GBFreq[42][73] = 6;
         * GBFreq[47][71] = 5; GBFreq[29][37] = 4; GBFreq[25][50] = 3; GBFreq[18][84] = 2; GBFreq[50][45] = 1;
         * GBFreq[48][46] = 0;
         */
        // GBFreq[43][89] = -1; GBFreq[54][68] = -2;
    }

    /**
     * 初始化 big5Freq
     */
    void initialize_big5Freq() {
        big5Freq[9][89] = 600;
        big5Freq[11][15] = 599;
        big5Freq[3][66] = 598;
        big5Freq[6][121] = 597;
        big5Freq[3][0] = 596;
        big5Freq[5][82] = 595;
        big5Freq[3][42] = 594;
        big5Freq[5][34] = 593;
        big5Freq[3][8] = 592;
        big5Freq[3][6] = 591;
        big5Freq[3][67] = 590;
        big5Freq[7][139] = 589;
        big5Freq[23][137] = 588;
        big5Freq[12][46] = 587;
        big5Freq[4][8] = 586;
        big5Freq[4][41] = 585;
        big5Freq[18][47] = 584;
        big5Freq[12][114] = 583;
        big5Freq[6][1] = 582;
        big5Freq[22][60] = 581;
        big5Freq[5][46] = 580;
        big5Freq[11][79] = 579;
        big5Freq[3][23] = 578;
        big5Freq[7][114] = 577;
        big5Freq[29][102] = 576;
        big5Freq[19][14] = 575;
        big5Freq[4][133] = 574;
        big5Freq[3][29] = 573;
        big5Freq[4][109] = 572;
        big5Freq[14][127] = 571;
        big5Freq[5][48] = 570;
        big5Freq[13][104] = 569;
        big5Freq[3][132] = 568;
        big5Freq[26][64] = 567;
        big5Freq[7][19] = 566;
        big5Freq[4][12] = 565;
        big5Freq[11][124] = 564;
        big5Freq[7][89] = 563;
        big5Freq[15][124] = 562;
        big5Freq[4][108] = 561;
        big5Freq[19][66] = 560;
        big5Freq[3][21] = 559;
        big5Freq[24][12] = 558;
        big5Freq[28][111] = 557;
        big5Freq[12][107] = 556;
        big5Freq[3][112] = 555;
        big5Freq[8][113] = 554;
        big5Freq[5][40] = 553;
        big5Freq[26][145] = 552;
        big5Freq[3][48] = 551;
        big5Freq[3][70] = 550;
        big5Freq[22][17] = 549;
        big5Freq[16][47] = 548;
        big5Freq[3][53] = 547;
        big5Freq[4][24] = 546;
        big5Freq[32][120] = 545;
        big5Freq[24][49] = 544;
        big5Freq[24][142] = 543;
        big5Freq[18][66] = 542;
        big5Freq[29][150] = 541;
        big5Freq[5][122] = 540;
        big5Freq[5][114] = 539;
        big5Freq[3][44] = 538;
        big5Freq[10][128] = 537;
        big5Freq[15][20] = 536;
        big5Freq[13][33] = 535;
        big5Freq[14][87] = 534;
        big5Freq[3][126] = 533;
        big5Freq[4][53] = 532;
        big5Freq[4][40] = 531;
        big5Freq[9][93] = 530;
        big5Freq[15][137] = 529;
        big5Freq[10][123] = 528;
        big5Freq[4][56] = 527;
        big5Freq[5][71] = 526;
        big5Freq[10][8] = 525;
        big5Freq[5][16] = 524;
        big5Freq[5][146] = 523;
        big5Freq[18][88] = 522;
        big5Freq[24][4] = 521;
        big5Freq[20][47] = 520;
        big5Freq[5][33] = 519;
        big5Freq[9][43] = 518;
        big5Freq[20][12] = 517;
        big5Freq[20][13] = 516;
        big5Freq[5][156] = 515;
        big5Freq[22][140] = 514;
        big5Freq[8][146] = 513;
        big5Freq[21][123] = 512;
        big5Freq[4][90] = 511;
        big5Freq[5][62] = 510;
        big5Freq[17][59] = 509;
        big5Freq[10][37] = 508;
        big5Freq[18][107] = 507;
        big5Freq[14][53] = 506;
        big5Freq[22][51] = 505;
        big5Freq[8][13] = 504;
        big5Freq[5][29] = 503;
        big5Freq[9][7] = 502;
        big5Freq[22][14] = 501;
        big5Freq[8][55] = 500;
        big5Freq[33][9] = 499;
        big5Freq[16][64] = 498;
        big5Freq[7][131] = 497;
        big5Freq[34][4] = 496;
        big5Freq[7][101] = 495;
        big5Freq[11][139] = 494;
        big5Freq[3][135] = 493;
        big5Freq[7][102] = 492;
        big5Freq[17][13] = 491;
        big5Freq[3][20] = 490;
        big5Freq[27][106] = 489;
        big5Freq[5][88] = 488;
        big5Freq[6][33] = 487;
        big5Freq[5][139] = 486;
        big5Freq[6][0] = 485;
        big5Freq[17][58] = 484;
        big5Freq[5][133] = 483;
        big5Freq[9][107] = 482;
        big5Freq[23][39] = 481;
        big5Freq[5][23] = 480;
        big5Freq[3][79] = 479;
        big5Freq[32][97] = 478;
        big5Freq[3][136] = 477;
        big5Freq[4][94] = 476;
        big5Freq[21][61] = 475;
        big5Freq[23][123] = 474;
        big5Freq[26][16] = 473;
        big5Freq[24][137] = 472;
        big5Freq[22][18] = 471;
        big5Freq[5][1] = 470;
        big5Freq[20][119] = 469;
        big5Freq[3][7] = 468;
        big5Freq[10][79] = 467;
        big5Freq[15][105] = 466;
        big5Freq[3][144] = 465;
        big5Freq[12][80] = 464;
        big5Freq[15][73] = 463;
        big5Freq[3][19] = 462;
        big5Freq[8][109] = 461;
        big5Freq[3][15] = 460;
        big5Freq[31][82] = 459;
        big5Freq[3][43] = 458;
        big5Freq[25][119] = 457;
        big5Freq[16][111] = 456;
        big5Freq[7][77] = 455;
        big5Freq[3][95] = 454;
        big5Freq[24][82] = 453;
        big5Freq[7][52] = 452;
        big5Freq[9][151] = 451;
        big5Freq[3][129] = 450;
        big5Freq[5][87] = 449;
        big5Freq[3][55] = 448;
        big5Freq[8][153] = 447;
        big5Freq[4][83] = 446;
        big5Freq[3][114] = 445;
        big5Freq[23][147] = 444;
        big5Freq[15][31] = 443;
        big5Freq[3][54] = 442;
        big5Freq[11][122] = 441;
        big5Freq[4][4] = 440;
        big5Freq[34][149] = 439;
        big5Freq[3][17] = 438;
        big5Freq[21][64] = 437;
        big5Freq[26][144] = 436;
        big5Freq[4][62] = 435;
        big5Freq[8][15] = 434;
        big5Freq[35][80] = 433;
        big5Freq[7][110] = 432;
        big5Freq[23][114] = 431;
        big5Freq[3][108] = 430;
        big5Freq[3][62] = 429;
        big5Freq[21][41] = 428;
        big5Freq[15][99] = 427;
        big5Freq[5][47] = 426;
        big5Freq[4][96] = 425;
        big5Freq[20][122] = 424;
        big5Freq[5][21] = 423;
        big5Freq[4][157] = 422;
        big5Freq[16][14] = 421;
        big5Freq[3][117] = 420;
        big5Freq[7][129] = 419;
        big5Freq[4][27] = 418;
        big5Freq[5][30] = 417;
        big5Freq[22][16] = 416;
        big5Freq[5][64] = 415;
        big5Freq[17][99] = 414;
        big5Freq[17][57] = 413;
        big5Freq[8][105] = 412;
        big5Freq[5][112] = 411;
        big5Freq[20][59] = 410;
        big5Freq[6][129] = 409;
        big5Freq[18][17] = 408;
        big5Freq[3][92] = 407;
        big5Freq[28][118] = 406;
        big5Freq[3][109] = 405;
        big5Freq[31][51] = 404;
        big5Freq[13][116] = 403;
        big5Freq[6][15] = 402;
        big5Freq[36][136] = 401;
        big5Freq[12][74] = 400;
        big5Freq[20][88] = 399;
        big5Freq[36][68] = 398;
        big5Freq[3][147] = 397;
        big5Freq[15][84] = 396;
        big5Freq[16][32] = 395;
        big5Freq[16][58] = 394;
        big5Freq[7][66] = 393;
        big5Freq[23][107] = 392;
        big5Freq[9][6] = 391;
        big5Freq[12][86] = 390;
        big5Freq[23][112] = 389;
        big5Freq[37][23] = 388;
        big5Freq[3][138] = 387;
        big5Freq[20][68] = 386;
        big5Freq[15][116] = 385;
        big5Freq[18][64] = 384;
        big5Freq[12][139] = 383;
        big5Freq[11][155] = 382;
        big5Freq[4][156] = 381;
        big5Freq[12][84] = 380;
        big5Freq[18][49] = 379;
        big5Freq[25][125] = 378;
        big5Freq[25][147] = 377;
        big5Freq[15][110] = 376;
        big5Freq[19][96] = 375;
        big5Freq[30][152] = 374;
        big5Freq[6][31] = 373;
        big5Freq[27][117] = 372;
        big5Freq[3][10] = 371;
        big5Freq[6][131] = 370;
        big5Freq[13][112] = 369;
        big5Freq[36][156] = 368;
        big5Freq[4][60] = 367;
        big5Freq[15][121] = 366;
        big5Freq[4][112] = 365;
        big5Freq[30][142] = 364;
        big5Freq[23][154] = 363;
        big5Freq[27][101] = 362;
        big5Freq[9][140] = 361;
        big5Freq[3][89] = 360;
        big5Freq[18][148] = 359;
        big5Freq[4][69] = 358;
        big5Freq[16][49] = 357;
        big5Freq[6][117] = 356;
        big5Freq[36][55] = 355;
        big5Freq[5][123] = 354;
        big5Freq[4][126] = 353;
        big5Freq[4][119] = 352;
        big5Freq[9][95] = 351;
        big5Freq[5][24] = 350;
        big5Freq[16][133] = 349;
        big5Freq[10][134] = 348;
        big5Freq[26][59] = 347;
        big5Freq[6][41] = 346;
        big5Freq[6][146] = 345;
        big5Freq[19][24] = 344;
        big5Freq[5][113] = 343;
        big5Freq[10][118] = 342;
        big5Freq[34][151] = 341;
        big5Freq[9][72] = 340;
        big5Freq[31][25] = 339;
        big5Freq[18][126] = 338;
        big5Freq[18][28] = 337;
        big5Freq[4][153] = 336;
        big5Freq[3][84] = 335;
        big5Freq[21][18] = 334;
        big5Freq[25][129] = 333;
        big5Freq[6][107] = 332;
        big5Freq[12][25] = 331;
        big5Freq[17][109] = 330;
        big5Freq[7][76] = 329;
        big5Freq[15][15] = 328;
        big5Freq[4][14] = 327;
        big5Freq[23][88] = 326;
        big5Freq[18][2] = 325;
        big5Freq[6][88] = 324;
        big5Freq[16][84] = 323;
        big5Freq[12][48] = 322;
        big5Freq[7][68] = 321;
        big5Freq[5][50] = 320;
        big5Freq[13][54] = 319;
        big5Freq[7][98] = 318;
        big5Freq[11][6] = 317;
        big5Freq[9][80] = 316;
        big5Freq[16][41] = 315;
        big5Freq[7][43] = 314;
        big5Freq[28][117] = 313;
        big5Freq[3][51] = 312;
        big5Freq[7][3] = 311;
        big5Freq[20][81] = 310;
        big5Freq[4][2] = 309;
        big5Freq[11][16] = 308;
        big5Freq[10][4] = 307;
        big5Freq[10][119] = 306;
        big5Freq[6][142] = 305;
        big5Freq[18][51] = 304;
        big5Freq[8][144] = 303;
        big5Freq[10][65] = 302;
        big5Freq[11][64] = 301;
        big5Freq[11][130] = 300;
        big5Freq[9][92] = 299;
        big5Freq[18][29] = 298;
        big5Freq[18][78] = 297;
        big5Freq[18][151] = 296;
        big5Freq[33][127] = 295;
        big5Freq[35][113] = 294;
        big5Freq[10][155] = 293;
        big5Freq[3][76] = 292;
        big5Freq[36][123] = 291;
        big5Freq[13][143] = 290;
        big5Freq[5][135] = 289;
        big5Freq[23][116] = 288;
        big5Freq[6][101] = 287;
        big5Freq[14][74] = 286;
        big5Freq[7][153] = 285;
        big5Freq[3][101] = 284;
        big5Freq[9][74] = 283;
        big5Freq[3][156] = 282;
        big5Freq[4][147] = 281;
        big5Freq[9][12] = 280;
        big5Freq[18][133] = 279;
        big5Freq[4][0] = 278;
        big5Freq[7][155] = 277;
        big5Freq[9][144] = 276;
        big5Freq[23][49] = 275;
        big5Freq[5][89] = 274;
        big5Freq[10][11] = 273;
        big5Freq[3][110] = 272;
        big5Freq[3][40] = 271;
        big5Freq[29][115] = 270;
        big5Freq[9][100] = 269;
        big5Freq[21][67] = 268;
        big5Freq[23][145] = 267;
        big5Freq[10][47] = 266;
        big5Freq[4][31] = 265;
        big5Freq[4][81] = 264;
        big5Freq[22][62] = 263;
        big5Freq[4][28] = 262;
        big5Freq[27][39] = 261;
        big5Freq[27][54] = 260;
        big5Freq[32][46] = 259;
        big5Freq[4][76] = 258;
        big5Freq[26][15] = 257;
        big5Freq[12][154] = 256;
        big5Freq[9][150] = 255;
        big5Freq[15][17] = 254;
        big5Freq[5][129] = 253;
        big5Freq[10][40] = 252;
        big5Freq[13][37] = 251;
        big5Freq[31][104] = 250;
        big5Freq[3][152] = 249;
        big5Freq[5][22] = 248;
        big5Freq[8][48] = 247;
        big5Freq[4][74] = 246;
        big5Freq[6][17] = 245;
        big5Freq[30][82] = 244;
        big5Freq[4][116] = 243;
        big5Freq[16][42] = 242;
        big5Freq[5][55] = 241;
        big5Freq[4][64] = 240;
        big5Freq[14][19] = 239;
        big5Freq[35][82] = 238;
        big5Freq[30][139] = 237;
        big5Freq[26][152] = 236;
        big5Freq[32][32] = 235;
        big5Freq[21][102] = 234;
        big5Freq[10][131] = 233;
        big5Freq[9][128] = 232;
        big5Freq[3][87] = 231;
        big5Freq[4][51] = 230;
        big5Freq[10][15] = 229;
        big5Freq[4][150] = 228;
        big5Freq[7][4] = 227;
        big5Freq[7][51] = 226;
        big5Freq[7][157] = 225;
        big5Freq[4][146] = 224;
        big5Freq[4][91] = 223;
        big5Freq[7][13] = 222;
        big5Freq[17][116] = 221;
        big5Freq[23][21] = 220;
        big5Freq[5][106] = 219;
        big5Freq[14][100] = 218;
        big5Freq[10][152] = 217;
        big5Freq[14][89] = 216;
        big5Freq[6][138] = 215;
        big5Freq[12][157] = 214;
        big5Freq[10][102] = 213;
        big5Freq[19][94] = 212;
        big5Freq[7][74] = 211;
        big5Freq[18][128] = 210;
        big5Freq[27][111] = 209;
        big5Freq[11][57] = 208;
        big5Freq[3][131] = 207;
        big5Freq[30][23] = 206;
        big5Freq[30][126] = 205;
        big5Freq[4][36] = 204;
        big5Freq[26][124] = 203;
        big5Freq[4][19] = 202;
        big5Freq[9][152] = 201;
        /*
         * Big5Freq[5][0] = 200; Big5Freq[26][57] = 199; Big5Freq[13][155] = 198; Big5Freq[3][38] = 197;
         * Big5Freq[9][155] = 196; Big5Freq[28][53] = 195; Big5Freq[15][71] = 194; Big5Freq[21][95] = 193;
         * Big5Freq[15][112] = 192; Big5Freq[14][138] = 191; Big5Freq[8][18] = 190; Big5Freq[20][151] = 189;
         * Big5Freq[37][27] = 188; Big5Freq[32][48] = 187; Big5Freq[23][66] = 186; Big5Freq[9][2] = 185;
         * Big5Freq[13][133] = 184; Big5Freq[7][127] = 183; Big5Freq[3][11] = 182; Big5Freq[12][118] = 181;
         * Big5Freq[13][101] = 180; Big5Freq[30][153] = 179; Big5Freq[4][65] = 178; Big5Freq[5][25] = 177;
         * Big5Freq[5][140] = 176; Big5Freq[6][25] = 175; Big5Freq[4][52] = 174; Big5Freq[30][156] = 173;
         * Big5Freq[16][13] = 172; Big5Freq[21][8] = 171; Big5Freq[19][74] = 170; Big5Freq[15][145] = 169;
         * Big5Freq[9][15] = 168; Big5Freq[13][82] = 167; Big5Freq[26][86] = 166; Big5Freq[18][52] = 165;
         * Big5Freq[6][109] = 164; Big5Freq[10][99] = 163; Big5Freq[18][101] = 162; Big5Freq[25][49] = 161;
         * Big5Freq[31][79] = 160; Big5Freq[28][20] = 159; Big5Freq[12][115] = 158; Big5Freq[15][66] = 157;
         * Big5Freq[11][104] = 156; Big5Freq[23][106] = 155; Big5Freq[34][157] = 154; Big5Freq[32][94] = 153;
         * Big5Freq[29][88] = 152; Big5Freq[10][46] = 151; Big5Freq[13][118] = 150; Big5Freq[20][37] = 149;
         * Big5Freq[12][30] = 148; Big5Freq[21][4] = 147; Big5Freq[16][33] = 146; Big5Freq[13][52] = 145;
         * Big5Freq[4][7] = 144; Big5Freq[21][49] = 143; Big5Freq[3][27] = 142; Big5Freq[16][91] = 141;
         * Big5Freq[5][155] = 140; Big5Freq[29][130] = 139; Big5Freq[3][125] = 138; Big5Freq[14][26] = 137;
         * Big5Freq[15][39] = 136; Big5Freq[24][110] = 135; Big5Freq[7][141] = 134; Big5Freq[21][15] = 133;
         * Big5Freq[32][104] = 132; Big5Freq[8][31] = 131; Big5Freq[34][112] = 130; Big5Freq[10][75] = 129;
         * Big5Freq[21][23] = 128; Big5Freq[34][131] = 127; Big5Freq[12][3] = 126; Big5Freq[10][62] = 125;
         * Big5Freq[9][120] = 124; Big5Freq[32][149] = 123; Big5Freq[8][44] = 122; Big5Freq[24][2] = 121;
         * Big5Freq[6][148] = 120; Big5Freq[15][103] = 119; Big5Freq[36][54] = 118; Big5Freq[36][134] = 117;
         * Big5Freq[11][7] = 116; Big5Freq[3][90] = 115; Big5Freq[36][73] = 114; Big5Freq[8][102] = 113;
         * Big5Freq[12][87] = 112; Big5Freq[25][64] = 111; Big5Freq[9][1] = 110; Big5Freq[24][121] = 109;
         * Big5Freq[5][75] = 108; Big5Freq[17][83] = 107; Big5Freq[18][57] = 106; Big5Freq[8][95] = 105;
         * Big5Freq[14][36] = 104; Big5Freq[28][113] = 103; Big5Freq[12][56] = 102; Big5Freq[14][61] = 101;
         * Big5Freq[25][138] = 100; Big5Freq[4][34] = 99; Big5Freq[11][152] = 98; Big5Freq[35][0] = 97;
         * Big5Freq[4][15] = 96; Big5Freq[8][82] = 95; Big5Freq[20][73] = 94; Big5Freq[25][52] = 93; Big5Freq[24][6]
         * = 92; Big5Freq[21][78] = 91; Big5Freq[17][32] = 90; Big5Freq[17][91] = 89; Big5Freq[5][76] = 88;
         * Big5Freq[15][60] = 87; Big5Freq[15][150] = 86; Big5Freq[5][80] = 85; Big5Freq[15][81] = 84;
         * Big5Freq[28][108] = 83; Big5Freq[18][14] = 82; Big5Freq[19][109] = 81; Big5Freq[28][133] = 80;
         * Big5Freq[21][97] = 79; Big5Freq[5][105] = 78; Big5Freq[18][114] = 77; Big5Freq[16][95] = 76;
         * Big5Freq[5][51] = 75; Big5Freq[3][148] = 74; Big5Freq[22][102] = 73; Big5Freq[4][123] = 72;
         * Big5Freq[8][88] = 71; Big5Freq[25][111] = 70; Big5Freq[8][149] = 69; Big5Freq[9][48] = 68;
         * Big5Freq[16][126] = 67; Big5Freq[33][150] = 66; Big5Freq[9][54] = 65; Big5Freq[29][104] = 64;
         * Big5Freq[3][3] = 63; Big5Freq[11][49] = 62; Big5Freq[24][109] = 61; Big5Freq[28][116] = 60;
         * Big5Freq[34][113] = 59; Big5Freq[5][3] = 58; Big5Freq[21][106] = 57; Big5Freq[4][98] = 56;
         * Big5Freq[12][135] = 55; Big5Freq[16][101] = 54; Big5Freq[12][147] = 53; Big5Freq[27][55] = 52;
         * Big5Freq[3][5] = 51; Big5Freq[11][101] = 50; Big5Freq[16][157] = 49; Big5Freq[22][114] = 48;
         * Big5Freq[18][46] = 47; Big5Freq[4][29] = 46; Big5Freq[8][103] = 45; Big5Freq[16][151] = 44;
         * Big5Freq[8][29] = 43; Big5Freq[15][114] = 42; Big5Freq[22][70] = 41; Big5Freq[13][121] = 40;
         * Big5Freq[7][112] = 39; Big5Freq[20][83] = 38; Big5Freq[3][36] = 37; Big5Freq[10][103] = 36;
         * Big5Freq[3][96] = 35; Big5Freq[21][79] = 34; Big5Freq[25][120] = 33; Big5Freq[29][121] = 32;
         * Big5Freq[23][71] = 31; Big5Freq[21][22] = 30; Big5Freq[18][89] = 29; Big5Freq[25][104] = 28;
         * Big5Freq[10][124] = 27; Big5Freq[26][4] = 26; Big5Freq[21][136] = 25; Big5Freq[6][112] = 24;
         * Big5Freq[12][103] = 23; Big5Freq[17][66] = 22; Big5Freq[13][151] = 21; Big5Freq[33][152] = 20;
         * Big5Freq[11][148] = 19; Big5Freq[13][57] = 18; Big5Freq[13][41] = 17; Big5Freq[7][60] = 16;
         * Big5Freq[21][29] = 15; Big5Freq[9][157] = 14; Big5Freq[24][95] = 13; Big5Freq[15][148] = 12;
         * Big5Freq[15][122] = 11; Big5Freq[6][125] = 10; Big5Freq[11][25] = 9; Big5Freq[20][55] = 8;
         * Big5Freq[19][84] = 7; Big5Freq[21][82] = 6; Big5Freq[24][3] = 5; Big5Freq[13][70] = 4; Big5Freq[6][21] =
         * 3; Big5Freq[21][86] = 2; Big5Freq[12][23] = 1; Big5Freq[3][85] = 0; EUC_TWFreq[45][90] = 600;
         */
    }

    /**
     * 初始化 big5pFreq
     */
    void initialize_big5pFreq() {
        big5pFreq[41][122] = 600;
        big5pFreq[35][0] = 599;
        big5pFreq[43][15] = 598;
        big5pFreq[35][99] = 597;
        big5pFreq[35][6] = 596;
        big5pFreq[35][8] = 595;
        big5pFreq[38][154] = 594;
        big5pFreq[37][34] = 593;
        big5pFreq[37][115] = 592;
        big5pFreq[36][12] = 591;
        big5pFreq[18][77] = 590;
        big5pFreq[35][100] = 589;
        big5pFreq[35][42] = 588;
        big5pFreq[120][75] = 587;
        big5pFreq[35][23] = 586;
        big5pFreq[13][72] = 585;
        big5pFreq[0][67] = 584;
        big5pFreq[39][172] = 583;
        big5pFreq[22][182] = 582;
        big5pFreq[15][186] = 581;
        big5pFreq[15][165] = 580;
        big5pFreq[35][44] = 579;
        big5pFreq[40][13] = 578;
        big5pFreq[38][1] = 577;
        big5pFreq[37][33] = 576;
        big5pFreq[36][24] = 575;
        big5pFreq[56][4] = 574;
        big5pFreq[35][29] = 573;
        big5pFreq[9][96] = 572;
        big5pFreq[37][62] = 571;
        big5pFreq[48][47] = 570;
        big5pFreq[51][14] = 569;
        big5pFreq[39][122] = 568;
        big5pFreq[44][46] = 567;
        big5pFreq[35][21] = 566;
        big5pFreq[36][8] = 565;
        big5pFreq[36][141] = 564;
        big5pFreq[3][81] = 563;
        big5pFreq[37][155] = 562;
        big5pFreq[42][84] = 561;
        big5pFreq[36][40] = 560;
        big5pFreq[35][103] = 559;
        big5pFreq[11][84] = 558;
        big5pFreq[45][33] = 557;
        big5pFreq[121][79] = 556;
        big5pFreq[2][77] = 555;
        big5pFreq[36][41] = 554;
        big5pFreq[37][47] = 553;
        big5pFreq[39][125] = 552;
        big5pFreq[37][26] = 551;
        big5pFreq[35][48] = 550;
        big5pFreq[35][28] = 549;
        big5pFreq[35][159] = 548;
        big5pFreq[37][40] = 547;
        big5pFreq[35][145] = 546;
        big5pFreq[37][147] = 545;
        big5pFreq[46][160] = 544;
        big5pFreq[37][46] = 543;
        big5pFreq[50][99] = 542;
        big5pFreq[52][13] = 541;
        big5pFreq[10][82] = 540;
        big5pFreq[35][169] = 539;
        big5pFreq[35][31] = 538;
        big5pFreq[47][31] = 537;
        big5pFreq[18][79] = 536;
        big5pFreq[16][113] = 535;
        big5pFreq[37][104] = 534;
        big5pFreq[39][134] = 533;
        big5pFreq[36][53] = 532;
        big5pFreq[38][0] = 531;
        big5pFreq[4][86] = 530;
        big5pFreq[54][17] = 529;
        big5pFreq[43][157] = 528;
        big5pFreq[35][165] = 527;
        big5pFreq[69][147] = 526;
        big5pFreq[117][95] = 525;
        big5pFreq[35][162] = 524;
        big5pFreq[35][17] = 523;
        big5pFreq[36][142] = 522;
        big5pFreq[36][4] = 521;
        big5pFreq[37][166] = 520;
        big5pFreq[35][168] = 519;
        big5pFreq[35][19] = 518;
        big5pFreq[37][48] = 517;
        big5pFreq[42][37] = 516;
        big5pFreq[40][146] = 515;
        big5pFreq[36][123] = 514;
        big5pFreq[22][41] = 513;
        big5pFreq[20][119] = 512;
        big5pFreq[2][74] = 511;
        big5pFreq[44][113] = 510;
        big5pFreq[35][125] = 509;
        big5pFreq[37][16] = 508;
        big5pFreq[35][20] = 507;
        big5pFreq[35][55] = 506;
        big5pFreq[37][145] = 505;
        big5pFreq[0][88] = 504;
        big5pFreq[3][94] = 503;
        big5pFreq[6][65] = 502;
        big5pFreq[26][15] = 501;
        big5pFreq[41][126] = 500;
        big5pFreq[36][129] = 499;
        big5pFreq[31][75] = 498;
        big5pFreq[19][61] = 497;
        big5pFreq[35][128] = 496;
        big5pFreq[29][79] = 495;
        big5pFreq[36][62] = 494;
        big5pFreq[37][189] = 493;
        big5pFreq[39][109] = 492;
        big5pFreq[39][135] = 491;
        big5pFreq[72][15] = 490;
        big5pFreq[47][106] = 489;
        big5pFreq[54][14] = 488;
        big5pFreq[24][52] = 487;
        big5pFreq[38][162] = 486;
        big5pFreq[41][43] = 485;
        big5pFreq[37][121] = 484;
        big5pFreq[14][66] = 483;
        big5pFreq[37][30] = 482;
        big5pFreq[35][7] = 481;
        big5pFreq[49][58] = 480;
        big5pFreq[43][188] = 479;
        big5pFreq[24][66] = 478;
        big5pFreq[35][171] = 477;
        big5pFreq[40][186] = 476;
        big5pFreq[39][164] = 475;
        big5pFreq[78][186] = 474;
        big5pFreq[8][72] = 473;
        big5pFreq[36][190] = 472;
        big5pFreq[35][53] = 471;
        big5pFreq[35][54] = 470;
        big5pFreq[22][159] = 469;
        big5pFreq[35][9] = 468;
        big5pFreq[41][140] = 467;
        big5pFreq[37][22] = 466;
        big5pFreq[48][97] = 465;
        big5pFreq[50][97] = 464;
        big5pFreq[36][127] = 463;
        big5pFreq[37][23] = 462;
        big5pFreq[40][55] = 461;
        big5pFreq[35][43] = 460;
        big5pFreq[26][22] = 459;
        big5pFreq[35][15] = 458;
        big5pFreq[72][179] = 457;
        big5pFreq[20][129] = 456;
        big5pFreq[52][101] = 455;
        big5pFreq[35][12] = 454;
        big5pFreq[42][156] = 453;
        big5pFreq[15][157] = 452;
        big5pFreq[50][140] = 451;
        big5pFreq[26][28] = 450;
        big5pFreq[54][51] = 449;
        big5pFreq[35][112] = 448;
        big5pFreq[36][116] = 447;
        big5pFreq[42][11] = 446;
        big5pFreq[37][172] = 445;
        big5pFreq[37][29] = 444;
        big5pFreq[44][107] = 443;
        big5pFreq[50][17] = 442;
        big5pFreq[39][107] = 441;
        big5pFreq[19][109] = 440;
        big5pFreq[36][60] = 439;
        big5pFreq[49][132] = 438;
        big5pFreq[26][16] = 437;
        big5pFreq[43][155] = 436;
        big5pFreq[37][120] = 435;
        big5pFreq[15][159] = 434;
        big5pFreq[43][6] = 433;
        big5pFreq[45][188] = 432;
        big5pFreq[35][38] = 431;
        big5pFreq[39][143] = 430;
        big5pFreq[48][144] = 429;
        big5pFreq[37][168] = 428;
        big5pFreq[37][1] = 427;
        big5pFreq[36][109] = 426;
        big5pFreq[46][53] = 425;
        big5pFreq[38][54] = 424;
        big5pFreq[36][0] = 423;
        big5pFreq[72][33] = 422;
        big5pFreq[42][8] = 421;
        big5pFreq[36][31] = 420;
        big5pFreq[35][150] = 419;
        big5pFreq[118][93] = 418;
        big5pFreq[37][61] = 417;
        big5pFreq[0][85] = 416;
        big5pFreq[36][27] = 415;
        big5pFreq[35][134] = 414;
        big5pFreq[36][145] = 413;
        big5pFreq[6][96] = 412;
        big5pFreq[36][14] = 411;
        big5pFreq[16][36] = 410;
        big5pFreq[15][175] = 409;
        big5pFreq[35][10] = 408;
        big5pFreq[36][189] = 407;
        big5pFreq[35][51] = 406;
        big5pFreq[35][109] = 405;
        big5pFreq[35][147] = 404;
        big5pFreq[35][180] = 403;
        big5pFreq[72][5] = 402;
        big5pFreq[36][107] = 401;
        big5pFreq[49][116] = 400;
        big5pFreq[73][30] = 399;
        big5pFreq[6][90] = 398;
        big5pFreq[2][70] = 397;
        big5pFreq[17][141] = 396;
        big5pFreq[35][62] = 395;
        big5pFreq[16][180] = 394;
        big5pFreq[4][91] = 393;
        big5pFreq[15][171] = 392;
        big5pFreq[35][177] = 391;
        big5pFreq[37][173] = 390;
        big5pFreq[16][121] = 389;
        big5pFreq[35][5] = 388;
        big5pFreq[46][122] = 387;
        big5pFreq[40][138] = 386;
        big5pFreq[50][49] = 385;
        big5pFreq[36][152] = 384;
        big5pFreq[13][43] = 383;
        big5pFreq[9][88] = 382;
        big5pFreq[36][159] = 381;
        big5pFreq[27][62] = 380;
        big5pFreq[40][18] = 379;
        big5pFreq[17][129] = 378;
        big5pFreq[43][97] = 377;
        big5pFreq[13][131] = 376;
        big5pFreq[46][107] = 375;
        big5pFreq[60][64] = 374;
        big5pFreq[36][179] = 373;
        big5pFreq[37][55] = 372;
        big5pFreq[41][173] = 371;
        big5pFreq[44][172] = 370;
        big5pFreq[23][187] = 369;
        big5pFreq[36][149] = 368;
        big5pFreq[17][125] = 367;
        big5pFreq[55][180] = 366;
        big5pFreq[51][129] = 365;
        big5pFreq[36][51] = 364;
        big5pFreq[37][122] = 363;
        big5pFreq[48][32] = 362;
        big5pFreq[51][99] = 361;
        big5pFreq[54][16] = 360;
        big5pFreq[41][183] = 359;
        big5pFreq[37][179] = 358;
        big5pFreq[38][179] = 357;
        big5pFreq[35][143] = 356;
        big5pFreq[37][24] = 355;
        big5pFreq[40][177] = 354;
        big5pFreq[47][117] = 353;
        big5pFreq[39][52] = 352;
        big5pFreq[22][99] = 351;
        big5pFreq[40][142] = 350;
        big5pFreq[36][49] = 349;
        big5pFreq[38][17] = 348;
        big5pFreq[39][188] = 347;
        big5pFreq[36][186] = 346;
        big5pFreq[35][189] = 345;
        big5pFreq[41][7] = 344;
        big5pFreq[18][91] = 343;
        big5pFreq[43][137] = 342;
        big5pFreq[35][142] = 341;
        big5pFreq[35][117] = 340;
        big5pFreq[39][138] = 339;
        big5pFreq[16][59] = 338;
        big5pFreq[39][174] = 337;
        big5pFreq[55][145] = 336;
        big5pFreq[37][21] = 335;
        big5pFreq[36][180] = 334;
        big5pFreq[37][156] = 333;
        big5pFreq[49][13] = 332;
        big5pFreq[41][107] = 331;
        big5pFreq[36][56] = 330;
        big5pFreq[53][8] = 329;
        big5pFreq[22][114] = 328;
        big5pFreq[5][95] = 327;
        big5pFreq[37][0] = 326;
        big5pFreq[26][183] = 325;
        big5pFreq[22][66] = 324;
        big5pFreq[35][58] = 323;
        big5pFreq[48][117] = 322;
        big5pFreq[36][102] = 321;
        big5pFreq[22][122] = 320;
        big5pFreq[35][11] = 319;
        big5pFreq[46][19] = 318;
        big5pFreq[22][49] = 317;
        big5pFreq[48][166] = 316;
        big5pFreq[41][125] = 315;
        big5pFreq[41][1] = 314;
        big5pFreq[35][178] = 313;
        big5pFreq[41][12] = 312;
        big5pFreq[26][167] = 311;
        big5pFreq[42][152] = 310;
        big5pFreq[42][46] = 309;
        big5pFreq[42][151] = 308;
        big5pFreq[20][135] = 307;
        big5pFreq[37][162] = 306;
        big5pFreq[37][50] = 305;
        big5pFreq[22][185] = 304;
        big5pFreq[36][166] = 303;
        big5pFreq[19][40] = 302;
        big5pFreq[22][107] = 301;
        big5pFreq[22][102] = 300;
        big5pFreq[57][162] = 299;
        big5pFreq[22][124] = 298;
        big5pFreq[37][138] = 297;
        big5pFreq[37][25] = 296;
        big5pFreq[0][69] = 295;
        big5pFreq[43][172] = 294;
        big5pFreq[42][167] = 293;
        big5pFreq[35][120] = 292;
        big5pFreq[41][128] = 291;
        big5pFreq[2][88] = 290;
        big5pFreq[20][123] = 289;
        big5pFreq[35][123] = 288;
        big5pFreq[36][28] = 287;
        big5pFreq[42][188] = 286;
        big5pFreq[42][164] = 285;
        big5pFreq[42][4] = 284;
        big5pFreq[43][57] = 283;
        big5pFreq[39][3] = 282;
        big5pFreq[42][3] = 281;
        big5pFreq[57][158] = 280;
        big5pFreq[35][146] = 279;
        big5pFreq[24][54] = 278;
        big5pFreq[13][110] = 277;
        big5pFreq[23][132] = 276;
        big5pFreq[26][102] = 275;
        big5pFreq[55][178] = 274;
        big5pFreq[17][117] = 273;
        big5pFreq[41][161] = 272;
        big5pFreq[38][150] = 271;
        big5pFreq[10][71] = 270;
        big5pFreq[47][60] = 269;
        big5pFreq[16][114] = 268;
        big5pFreq[21][47] = 267;
        big5pFreq[39][101] = 266;
        big5pFreq[18][45] = 265;
        big5pFreq[40][121] = 264;
        big5pFreq[45][41] = 263;
        big5pFreq[22][167] = 262;
        big5pFreq[26][149] = 261;
        big5pFreq[15][189] = 260;
        big5pFreq[41][177] = 259;
        big5pFreq[46][36] = 258;
        big5pFreq[20][40] = 257;
        big5pFreq[41][54] = 256;
        big5pFreq[3][87] = 255;
        big5pFreq[40][16] = 254;
        big5pFreq[42][15] = 253;
        big5pFreq[11][83] = 252;
        big5pFreq[0][94] = 251;
        big5pFreq[122][81] = 250;
        big5pFreq[41][26] = 249;
        big5pFreq[36][34] = 248;
        big5pFreq[44][148] = 247;
        big5pFreq[35][3] = 246;
        big5pFreq[36][114] = 245;
        big5pFreq[42][112] = 244;
        big5pFreq[35][183] = 243;
        big5pFreq[49][73] = 242;
        big5pFreq[39][2] = 241;
        big5pFreq[38][121] = 240;
        big5pFreq[44][114] = 239;
        big5pFreq[49][32] = 238;
        big5pFreq[1][65] = 237;
        big5pFreq[38][25] = 236;
        big5pFreq[39][4] = 235;
        big5pFreq[42][62] = 234;
        big5pFreq[35][40] = 233;
        big5pFreq[24][2] = 232;
        big5pFreq[53][49] = 231;
        big5pFreq[41][133] = 230;
        big5pFreq[43][134] = 229;
        big5pFreq[3][83] = 228;
        big5pFreq[38][158] = 227;
        big5pFreq[24][17] = 226;
        big5pFreq[52][59] = 225;
        big5pFreq[38][41] = 224;
        big5pFreq[37][127] = 223;
        big5pFreq[22][175] = 222;
        big5pFreq[44][30] = 221;
        big5pFreq[47][178] = 220;
        big5pFreq[43][99] = 219;
        big5pFreq[19][4] = 218;
        big5pFreq[37][97] = 217;
        big5pFreq[38][181] = 216;
        big5pFreq[45][103] = 215;
        big5pFreq[1][86] = 214;
        big5pFreq[40][15] = 213;
        big5pFreq[22][136] = 212;
        big5pFreq[75][165] = 211;
        big5pFreq[36][15] = 210;
        big5pFreq[46][80] = 209;
        big5pFreq[59][55] = 208;
        big5pFreq[37][108] = 207;
        big5pFreq[21][109] = 206;
        big5pFreq[24][165] = 205;
        big5pFreq[79][158] = 204;
        big5pFreq[44][139] = 203;
        big5pFreq[36][124] = 202;
        big5pFreq[42][185] = 201;
        big5pFreq[39][186] = 200;
        big5pFreq[22][128] = 199;
        big5pFreq[40][44] = 198;
        big5pFreq[41][105] = 197;
        big5pFreq[1][70] = 196;
        big5pFreq[1][68] = 195;
        big5pFreq[53][22] = 194;
        big5pFreq[36][54] = 193;
        big5pFreq[47][147] = 192;
        big5pFreq[35][36] = 191;
        big5pFreq[35][185] = 190;
        big5pFreq[45][37] = 189;
        big5pFreq[43][163] = 188;
        big5pFreq[56][115] = 187;
        big5pFreq[38][164] = 186;
        big5pFreq[35][141] = 185;
        big5pFreq[42][132] = 184;
        big5pFreq[46][120] = 183;
        big5pFreq[69][142] = 182;
        big5pFreq[38][175] = 181;
        big5pFreq[22][112] = 180;
        big5pFreq[38][142] = 179;
        big5pFreq[40][37] = 178;
        big5pFreq[37][109] = 177;
        big5pFreq[40][144] = 176;
        big5pFreq[44][117] = 175;
        big5pFreq[35][181] = 174;
        big5pFreq[26][105] = 173;
        big5pFreq[16][48] = 172;
        big5pFreq[44][122] = 171;
        big5pFreq[12][86] = 170;
        big5pFreq[84][53] = 169;
        big5pFreq[17][44] = 168;
        big5pFreq[59][54] = 167;
        big5pFreq[36][98] = 166;
        big5pFreq[45][115] = 165;
        big5pFreq[73][9] = 164;
        big5pFreq[44][123] = 163;
        big5pFreq[37][188] = 162;
        big5pFreq[51][117] = 161;
        big5pFreq[15][156] = 160;
        big5pFreq[36][155] = 159;
        big5pFreq[44][25] = 158;
        big5pFreq[38][12] = 157;
        big5pFreq[38][140] = 156;
        big5pFreq[23][4] = 155;
        big5pFreq[45][149] = 154;
        big5pFreq[22][189] = 153;
        big5pFreq[38][147] = 152;
        big5pFreq[27][5] = 151;
        big5pFreq[22][42] = 150;
        big5pFreq[3][68] = 149;
        big5pFreq[39][51] = 148;
        big5pFreq[36][29] = 147;
        big5pFreq[20][108] = 146;
        big5pFreq[50][57] = 145;
        big5pFreq[55][104] = 144;
        big5pFreq[22][46] = 143;
        big5pFreq[18][164] = 142;
        big5pFreq[50][159] = 141;
        big5pFreq[85][131] = 140;
        big5pFreq[26][79] = 139;
        big5pFreq[38][100] = 138;
        big5pFreq[53][112] = 137;
        big5pFreq[20][190] = 136;
        big5pFreq[14][69] = 135;
        big5pFreq[23][11] = 134;
        big5pFreq[40][114] = 133;
        big5pFreq[40][148] = 132;
        big5pFreq[53][130] = 131;
        big5pFreq[36][2] = 130;
        big5pFreq[66][82] = 129;
        big5pFreq[45][166] = 128;
        big5pFreq[4][88] = 127;
        big5pFreq[16][57] = 126;
        big5pFreq[22][116] = 125;
        big5pFreq[36][108] = 124;
        big5pFreq[13][48] = 123;
        big5pFreq[54][12] = 122;
        big5pFreq[40][136] = 121;
        big5pFreq[36][128] = 120;
        big5pFreq[23][6] = 119;
        big5pFreq[38][125] = 118;
        big5pFreq[45][154] = 117;
        big5pFreq[51][127] = 116;
        big5pFreq[44][163] = 115;
        big5pFreq[16][173] = 114;
        big5pFreq[43][49] = 113;
        big5pFreq[20][112] = 112;
        big5pFreq[15][168] = 111;
        big5pFreq[35][129] = 110;
        big5pFreq[20][45] = 109;
        big5pFreq[38][10] = 108;
        big5pFreq[57][171] = 107;
        big5pFreq[44][190] = 106;
        big5pFreq[40][56] = 105;
        big5pFreq[36][156] = 104;
        big5pFreq[3][88] = 103;
        big5pFreq[50][122] = 102;
        big5pFreq[36][7] = 101;
        big5pFreq[39][43] = 100;
        big5pFreq[15][166] = 99;
        big5pFreq[42][136] = 98;
        big5pFreq[22][131] = 97;
        big5pFreq[44][23] = 96;
        big5pFreq[54][147] = 95;
        big5pFreq[41][32] = 94;
        big5pFreq[23][121] = 93;
        big5pFreq[39][108] = 92;
        big5pFreq[2][78] = 91;
        big5pFreq[40][155] = 90;
        big5pFreq[55][51] = 89;
        big5pFreq[19][34] = 88;
        big5pFreq[48][128] = 87;
        big5pFreq[48][159] = 86;
        big5pFreq[20][70] = 85;
        big5pFreq[34][71] = 84;
        big5pFreq[16][31] = 83;
        big5pFreq[42][157] = 82;
        big5pFreq[20][44] = 81;
        big5pFreq[11][92] = 80;
        big5pFreq[44][180] = 79;
        big5pFreq[84][33] = 78;
        big5pFreq[16][116] = 77;
        big5pFreq[61][163] = 76;
        big5pFreq[35][164] = 75;
        big5pFreq[36][42] = 74;
        big5pFreq[13][40] = 73;
        big5pFreq[43][176] = 72;
        big5pFreq[2][66] = 71;
        big5pFreq[20][133] = 70;
        big5pFreq[36][65] = 69;
        big5pFreq[38][33] = 68;
        big5pFreq[12][91] = 67;
        big5pFreq[36][26] = 66;
        big5pFreq[15][174] = 65;
        big5pFreq[77][32] = 64;
        big5pFreq[16][1] = 63;
        big5pFreq[25][86] = 62;
        big5pFreq[17][13] = 61;
        big5pFreq[5][75] = 60;
        big5pFreq[36][52] = 59;
        big5pFreq[51][164] = 58;
        big5pFreq[12][85] = 57;
        big5pFreq[39][168] = 56;
        big5pFreq[43][16] = 55;
        big5pFreq[40][69] = 54;
        big5pFreq[26][108] = 53;
        big5pFreq[51][56] = 52;
        big5pFreq[16][37] = 51;
        big5pFreq[40][29] = 50;
        big5pFreq[46][171] = 49;
        big5pFreq[40][128] = 48;
        big5pFreq[72][114] = 47;
        big5pFreq[21][103] = 46;
        big5pFreq[22][44] = 45;
        big5pFreq[40][115] = 44;
        big5pFreq[43][7] = 43;
        big5pFreq[43][153] = 42;
        big5pFreq[17][20] = 41;
        big5pFreq[16][49] = 40;
        big5pFreq[36][57] = 39;
        big5pFreq[18][38] = 38;
        big5pFreq[45][184] = 37;
        big5pFreq[37][167] = 36;
        big5pFreq[26][106] = 35;
        big5pFreq[61][121] = 34;
        big5pFreq[89][140] = 33;
        big5pFreq[46][61] = 32;
        big5pFreq[39][163] = 31;
        big5pFreq[40][62] = 30;
        big5pFreq[38][165] = 29;
        big5pFreq[47][37] = 28;
        big5pFreq[18][155] = 27;
        big5pFreq[20][33] = 26;
        big5pFreq[29][90] = 25;
        big5pFreq[20][103] = 24;
        big5pFreq[37][51] = 23;
        big5pFreq[57][0] = 22;
        big5pFreq[40][31] = 21;
        big5pFreq[45][32] = 20;
        big5pFreq[59][23] = 19;
        big5pFreq[18][47] = 18;
        big5pFreq[45][134] = 17;
        big5pFreq[37][59] = 16;
        big5pFreq[21][128] = 15;
        big5pFreq[36][106] = 14;
        big5pFreq[31][39] = 13;
        big5pFreq[40][182] = 12;
        big5pFreq[52][155] = 11;
        big5pFreq[42][166] = 10;
        big5pFreq[35][27] = 9;
        big5pFreq[38][3] = 8;
        big5pFreq[13][44] = 7;
        big5pFreq[58][157] = 6;
        big5pFreq[47][51] = 5;
        big5pFreq[41][37] = 4;
        big5pFreq[41][172] = 3;
        big5pFreq[51][165] = 2;
        big5pFreq[15][161] = 1;
        big5pFreq[24][181] = 0;
    }

    /**
     * 初始化 eucTwFreq
     */
    void initialize_eucTwFreq() {
        eucTwFreq[48][49] = 599;
        eucTwFreq[35][65] = 598;
        eucTwFreq[41][27] = 597;
        eucTwFreq[35][0] = 596;
        eucTwFreq[39][19] = 595;
        eucTwFreq[35][42] = 594;
        eucTwFreq[38][66] = 593;
        eucTwFreq[35][8] = 592;
        eucTwFreq[35][6] = 591;
        eucTwFreq[35][66] = 590;
        eucTwFreq[43][14] = 589;
        eucTwFreq[69][80] = 588;
        eucTwFreq[50][48] = 587;
        eucTwFreq[36][71] = 586;
        eucTwFreq[37][10] = 585;
        eucTwFreq[60][52] = 584;
        eucTwFreq[51][21] = 583;
        eucTwFreq[40][2] = 582;
        eucTwFreq[67][35] = 581;
        eucTwFreq[38][78] = 580;
        eucTwFreq[49][18] = 579;
        eucTwFreq[35][23] = 578;
        eucTwFreq[42][83] = 577;
        eucTwFreq[79][47] = 576;
        eucTwFreq[61][82] = 575;
        eucTwFreq[38][7] = 574;
        eucTwFreq[35][29] = 573;
        eucTwFreq[37][77] = 572;
        eucTwFreq[54][67] = 571;
        eucTwFreq[38][80] = 570;
        eucTwFreq[52][74] = 569;
        eucTwFreq[36][37] = 568;
        eucTwFreq[74][8] = 567;
        eucTwFreq[41][83] = 566;
        eucTwFreq[36][75] = 565;
        eucTwFreq[49][63] = 564;
        eucTwFreq[42][58] = 563;
        eucTwFreq[56][33] = 562;
        eucTwFreq[37][76] = 561;
        eucTwFreq[62][39] = 560;
        eucTwFreq[35][21] = 559;
        eucTwFreq[70][19] = 558;
        eucTwFreq[77][88] = 557;
        eucTwFreq[51][14] = 556;
        eucTwFreq[36][17] = 555;
        eucTwFreq[44][51] = 554;
        eucTwFreq[38][72] = 553;
        eucTwFreq[74][90] = 552;
        eucTwFreq[35][48] = 551;
        eucTwFreq[35][69] = 550;
        eucTwFreq[66][86] = 549;
        eucTwFreq[57][20] = 548;
        eucTwFreq[35][53] = 547;
        eucTwFreq[36][87] = 546;
        eucTwFreq[84][67] = 545;
        eucTwFreq[70][56] = 544;
        eucTwFreq[71][54] = 543;
        eucTwFreq[60][70] = 542;
        eucTwFreq[80][1] = 541;
        eucTwFreq[39][59] = 540;
        eucTwFreq[39][51] = 539;
        eucTwFreq[35][44] = 538;
        eucTwFreq[48][4] = 537;
        eucTwFreq[55][24] = 536;
        eucTwFreq[52][4] = 535;
        eucTwFreq[54][26] = 534;
        eucTwFreq[36][31] = 533;
        eucTwFreq[37][22] = 532;
        eucTwFreq[37][9] = 531;
        eucTwFreq[46][0] = 530;
        eucTwFreq[56][46] = 529;
        eucTwFreq[47][93] = 528;
        eucTwFreq[37][25] = 527;
        eucTwFreq[39][8] = 526;
        eucTwFreq[46][73] = 525;
        eucTwFreq[38][48] = 524;
        eucTwFreq[39][83] = 523;
        eucTwFreq[60][92] = 522;
        eucTwFreq[70][11] = 521;
        eucTwFreq[63][84] = 520;
        eucTwFreq[38][65] = 519;
        eucTwFreq[45][45] = 518;
        eucTwFreq[63][49] = 517;
        eucTwFreq[63][50] = 516;
        eucTwFreq[39][93] = 515;
        eucTwFreq[68][20] = 514;
        eucTwFreq[44][84] = 513;
        eucTwFreq[66][34] = 512;
        eucTwFreq[37][58] = 511;
        eucTwFreq[39][0] = 510;
        eucTwFreq[59][1] = 509;
        eucTwFreq[47][8] = 508;
        eucTwFreq[61][17] = 507;
        eucTwFreq[53][87] = 506;
        eucTwFreq[67][26] = 505;
        eucTwFreq[43][46] = 504;
        eucTwFreq[38][61] = 503;
        eucTwFreq[45][9] = 502;
        eucTwFreq[66][83] = 501;
        eucTwFreq[43][88] = 500;
        eucTwFreq[85][20] = 499;
        eucTwFreq[57][36] = 498;
        eucTwFreq[43][6] = 497;
        eucTwFreq[86][77] = 496;
        eucTwFreq[42][70] = 495;
        eucTwFreq[49][78] = 494;
        eucTwFreq[36][40] = 493;
        eucTwFreq[42][71] = 492;
        eucTwFreq[58][49] = 491;
        eucTwFreq[35][20] = 490;
        eucTwFreq[76][20] = 489;
        eucTwFreq[39][25] = 488;
        eucTwFreq[40][34] = 487;
        eucTwFreq[39][76] = 486;
        eucTwFreq[40][1] = 485;
        eucTwFreq[59][0] = 484;
        eucTwFreq[39][70] = 483;
        eucTwFreq[46][14] = 482;
        eucTwFreq[68][77] = 481;
        eucTwFreq[38][55] = 480;
        eucTwFreq[35][78] = 479;
        eucTwFreq[84][44] = 478;
        eucTwFreq[36][41] = 477;
        eucTwFreq[37][62] = 476;
        eucTwFreq[65][67] = 475;
        eucTwFreq[69][66] = 474;
        eucTwFreq[73][55] = 473;
        eucTwFreq[71][49] = 472;
        eucTwFreq[66][87] = 471;
        eucTwFreq[38][33] = 470;
        eucTwFreq[64][61] = 469;
        eucTwFreq[35][7] = 468;
        eucTwFreq[47][49] = 467;
        eucTwFreq[56][14] = 466;
        eucTwFreq[36][49] = 465;
        eucTwFreq[50][81] = 464;
        eucTwFreq[55][76] = 463;
        eucTwFreq[35][19] = 462;
        eucTwFreq[44][47] = 461;
        eucTwFreq[35][15] = 460;
        eucTwFreq[82][59] = 459;
        eucTwFreq[35][43] = 458;
        eucTwFreq[73][0] = 457;
        eucTwFreq[57][83] = 456;
        eucTwFreq[42][46] = 455;
        eucTwFreq[36][0] = 454;
        eucTwFreq[70][88] = 453;
        eucTwFreq[42][22] = 452;
        eucTwFreq[46][58] = 451;
        eucTwFreq[36][34] = 450;
        eucTwFreq[39][24] = 449;
        eucTwFreq[35][55] = 448;
        eucTwFreq[44][91] = 447;
        eucTwFreq[37][51] = 446;
        eucTwFreq[36][19] = 445;
        eucTwFreq[69][90] = 444;
        eucTwFreq[55][35] = 443;
        eucTwFreq[35][54] = 442;
        eucTwFreq[49][61] = 441;
        eucTwFreq[36][67] = 440;
        eucTwFreq[88][34] = 439;
        eucTwFreq[35][17] = 438;
        eucTwFreq[65][69] = 437;
        eucTwFreq[74][89] = 436;
        eucTwFreq[37][31] = 435;
        eucTwFreq[43][48] = 434;
        eucTwFreq[89][27] = 433;
        eucTwFreq[42][79] = 432;
        eucTwFreq[69][57] = 431;
        eucTwFreq[36][13] = 430;
        eucTwFreq[35][62] = 429;
        eucTwFreq[65][47] = 428;
        eucTwFreq[56][8] = 427;
        eucTwFreq[38][79] = 426;
        eucTwFreq[37][64] = 425;
        eucTwFreq[64][64] = 424;
        eucTwFreq[38][53] = 423;
        eucTwFreq[38][31] = 422;
        eucTwFreq[56][81] = 421;
        eucTwFreq[36][22] = 420;
        eucTwFreq[43][4] = 419;
        eucTwFreq[36][90] = 418;
        eucTwFreq[38][62] = 417;
        eucTwFreq[66][85] = 416;
        eucTwFreq[39][1] = 415;
        eucTwFreq[59][40] = 414;
        eucTwFreq[58][93] = 413;
        eucTwFreq[44][43] = 412;
        eucTwFreq[39][49] = 411;
        eucTwFreq[64][2] = 410;
        eucTwFreq[41][35] = 409;
        eucTwFreq[60][22] = 408;
        eucTwFreq[35][91] = 407;
        eucTwFreq[78][1] = 406;
        eucTwFreq[36][14] = 405;
        eucTwFreq[82][29] = 404;
        eucTwFreq[52][86] = 403;
        eucTwFreq[40][16] = 402;
        eucTwFreq[91][52] = 401;
        eucTwFreq[50][75] = 400;
        eucTwFreq[64][30] = 399;
        eucTwFreq[90][78] = 398;
        eucTwFreq[36][52] = 397;
        eucTwFreq[55][87] = 396;
        eucTwFreq[57][5] = 395;
        eucTwFreq[57][31] = 394;
        eucTwFreq[42][35] = 393;
        eucTwFreq[69][50] = 392;
        eucTwFreq[45][8] = 391;
        eucTwFreq[50][87] = 390;
        eucTwFreq[69][55] = 389;
        eucTwFreq[92][3] = 388;
        eucTwFreq[36][43] = 387;
        eucTwFreq[64][10] = 386;
        eucTwFreq[56][25] = 385;
        eucTwFreq[60][68] = 384;
        eucTwFreq[51][46] = 383;
        eucTwFreq[50][0] = 382;
        eucTwFreq[38][30] = 381;
        eucTwFreq[50][85] = 380;
        eucTwFreq[60][54] = 379;
        eucTwFreq[73][6] = 378;
        eucTwFreq[73][28] = 377;
        eucTwFreq[56][19] = 376;
        eucTwFreq[62][69] = 375;
        eucTwFreq[81][66] = 374;
        eucTwFreq[40][32] = 373;
        eucTwFreq[76][31] = 372;
        eucTwFreq[35][10] = 371;
        eucTwFreq[41][37] = 370;
        eucTwFreq[52][82] = 369;
        eucTwFreq[91][72] = 368;
        eucTwFreq[37][29] = 367;
        eucTwFreq[56][30] = 366;
        eucTwFreq[37][80] = 365;
        eucTwFreq[81][56] = 364;
        eucTwFreq[70][3] = 363;
        eucTwFreq[76][15] = 362;
        eucTwFreq[46][47] = 361;
        eucTwFreq[35][88] = 360;
        eucTwFreq[61][58] = 359;
        eucTwFreq[37][37] = 358;
        eucTwFreq[57][22] = 357;
        eucTwFreq[41][23] = 356;
        eucTwFreq[90][66] = 355;
        eucTwFreq[39][60] = 354;
        eucTwFreq[38][0] = 353;
        eucTwFreq[37][87] = 352;
        eucTwFreq[46][2] = 351;
        eucTwFreq[38][56] = 350;
        eucTwFreq[58][11] = 349;
        eucTwFreq[48][10] = 348;
        eucTwFreq[74][4] = 347;
        eucTwFreq[40][42] = 346;
        eucTwFreq[41][52] = 345;
        eucTwFreq[61][92] = 344;
        eucTwFreq[39][50] = 343;
        eucTwFreq[47][88] = 342;
        eucTwFreq[88][36] = 341;
        eucTwFreq[45][73] = 340;
        eucTwFreq[82][3] = 339;
        eucTwFreq[61][36] = 338;
        eucTwFreq[60][33] = 337;
        eucTwFreq[38][27] = 336;
        eucTwFreq[35][83] = 335;
        eucTwFreq[65][24] = 334;
        eucTwFreq[73][10] = 333;
        eucTwFreq[41][13] = 332;
        eucTwFreq[50][27] = 331;
        eucTwFreq[59][50] = 330;
        eucTwFreq[42][45] = 329;
        eucTwFreq[55][19] = 328;
        eucTwFreq[36][77] = 327;
        eucTwFreq[69][31] = 326;
        eucTwFreq[60][7] = 325;
        eucTwFreq[40][88] = 324;
        eucTwFreq[57][56] = 323;
        eucTwFreq[50][50] = 322;
        eucTwFreq[42][37] = 321;
        eucTwFreq[38][82] = 320;
        eucTwFreq[52][25] = 319;
        eucTwFreq[42][67] = 318;
        eucTwFreq[48][40] = 317;
        eucTwFreq[45][81] = 316;
        eucTwFreq[57][14] = 315;
        eucTwFreq[42][13] = 314;
        eucTwFreq[78][0] = 313;
        eucTwFreq[35][51] = 312;
        eucTwFreq[41][67] = 311;
        eucTwFreq[64][23] = 310;
        eucTwFreq[36][65] = 309;
        eucTwFreq[48][50] = 308;
        eucTwFreq[46][69] = 307;
        eucTwFreq[47][89] = 306;
        eucTwFreq[41][48] = 305;
        eucTwFreq[60][56] = 304;
        eucTwFreq[44][82] = 303;
        eucTwFreq[47][35] = 302;
        eucTwFreq[49][3] = 301;
        eucTwFreq[49][69] = 300;
        eucTwFreq[45][93] = 299;
        eucTwFreq[60][34] = 298;
        eucTwFreq[60][82] = 297;
        eucTwFreq[61][61] = 296;
        eucTwFreq[86][42] = 295;
        eucTwFreq[89][60] = 294;
        eucTwFreq[48][31] = 293;
        eucTwFreq[35][75] = 292;
        eucTwFreq[91][39] = 291;
        eucTwFreq[53][19] = 290;
        eucTwFreq[39][72] = 289;
        eucTwFreq[69][59] = 288;
        eucTwFreq[41][7] = 287;
        eucTwFreq[54][13] = 286;
        eucTwFreq[43][28] = 285;
        eucTwFreq[36][6] = 284;
        eucTwFreq[45][75] = 283;
        eucTwFreq[36][61] = 282;
        eucTwFreq[38][21] = 281;
        eucTwFreq[45][14] = 280;
        eucTwFreq[61][43] = 279;
        eucTwFreq[36][63] = 278;
        eucTwFreq[43][30] = 277;
        eucTwFreq[46][51] = 276;
        eucTwFreq[68][87] = 275;
        eucTwFreq[39][26] = 274;
        eucTwFreq[46][76] = 273;
        eucTwFreq[36][15] = 272;
        eucTwFreq[35][40] = 271;
        eucTwFreq[79][60] = 270;
        eucTwFreq[46][7] = 269;
        eucTwFreq[65][72] = 268;
        eucTwFreq[69][88] = 267;
        eucTwFreq[47][18] = 266;
        eucTwFreq[37][0] = 265;
        eucTwFreq[37][49] = 264;
        eucTwFreq[67][37] = 263;
        eucTwFreq[36][91] = 262;
        eucTwFreq[75][48] = 261;
        eucTwFreq[75][63] = 260;
        eucTwFreq[83][87] = 259;
        eucTwFreq[37][44] = 258;
        eucTwFreq[73][54] = 257;
        eucTwFreq[51][61] = 256;
        eucTwFreq[46][57] = 255;
        eucTwFreq[55][21] = 254;
        eucTwFreq[39][66] = 253;
        eucTwFreq[47][11] = 252;
        eucTwFreq[52][8] = 251;
        eucTwFreq[82][81] = 250;
        eucTwFreq[36][57] = 249;
        eucTwFreq[38][54] = 248;
        eucTwFreq[43][81] = 247;
        eucTwFreq[37][42] = 246;
        eucTwFreq[40][18] = 245;
        eucTwFreq[80][90] = 244;
        eucTwFreq[37][84] = 243;
        eucTwFreq[57][15] = 242;
        eucTwFreq[38][87] = 241;
        eucTwFreq[37][32] = 240;
        eucTwFreq[53][53] = 239;
        eucTwFreq[89][29] = 238;
        eucTwFreq[81][53] = 237;
        eucTwFreq[75][3] = 236;
        eucTwFreq[83][73] = 235;
        eucTwFreq[66][13] = 234;
        eucTwFreq[48][7] = 233;
        eucTwFreq[46][35] = 232;
        eucTwFreq[35][86] = 231;
        eucTwFreq[37][20] = 230;
        eucTwFreq[46][80] = 229;
        eucTwFreq[38][24] = 228;
        eucTwFreq[41][68] = 227;
        eucTwFreq[42][21] = 226;
        eucTwFreq[43][32] = 225;
        eucTwFreq[38][20] = 224;
        eucTwFreq[37][59] = 223;
        eucTwFreq[41][77] = 222;
        eucTwFreq[59][57] = 221;
        eucTwFreq[68][59] = 220;
        eucTwFreq[39][43] = 219;
        eucTwFreq[54][39] = 218;
        eucTwFreq[48][28] = 217;
        eucTwFreq[54][28] = 216;
        eucTwFreq[41][44] = 215;
        eucTwFreq[51][64] = 214;
        eucTwFreq[47][72] = 213;
        eucTwFreq[62][67] = 212;
        eucTwFreq[42][43] = 211;
        eucTwFreq[61][38] = 210;
        eucTwFreq[76][25] = 209;
        eucTwFreq[48][91] = 208;
        eucTwFreq[36][36] = 207;
        eucTwFreq[80][32] = 206;
        eucTwFreq[81][40] = 205;
        eucTwFreq[37][5] = 204;
        eucTwFreq[74][69] = 203;
        eucTwFreq[36][82] = 202;
        eucTwFreq[46][59] = 201;
        /*
         * EUC_TWFreq[38][32] = 200; EUC_TWFreq[74][2] = 199; EUC_TWFreq[53][31] = 198; EUC_TWFreq[35][38] = 197;
         * EUC_TWFreq[46][62] = 196; EUC_TWFreq[77][31] = 195; EUC_TWFreq[55][74] = 194; EUC_TWFreq[66][6] = 193;
         * EUC_TWFreq[56][21] = 192; EUC_TWFreq[54][78] = 191; EUC_TWFreq[43][51] = 190; EUC_TWFreq[64][93] = 189;
         * EUC_TWFreq[92][7] = 188; EUC_TWFreq[83][89] = 187; EUC_TWFreq[69][9] = 186; EUC_TWFreq[45][4] = 185;
         * EUC_TWFreq[53][9] = 184; EUC_TWFreq[43][2] = 183; EUC_TWFreq[35][11] = 182; EUC_TWFreq[51][25] = 181;
         * EUC_TWFreq[52][71] = 180; EUC_TWFreq[81][67] = 179; EUC_TWFreq[37][33] = 178; EUC_TWFreq[38][57] = 177;
         * EUC_TWFreq[39][77] = 176; EUC_TWFreq[40][26] = 175; EUC_TWFreq[37][21] = 174; EUC_TWFreq[81][70] = 173;
         * EUC_TWFreq[56][80] = 172; EUC_TWFreq[65][14] = 171; EUC_TWFreq[62][47] = 170; EUC_TWFreq[56][54] = 169;
         * EUC_TWFreq[45][17] = 168; EUC_TWFreq[52][52] = 167; EUC_TWFreq[74][30] = 166; EUC_TWFreq[60][57] = 165;
         * EUC_TWFreq[41][15] = 164; EUC_TWFreq[47][69] = 163; EUC_TWFreq[61][11] = 162; EUC_TWFreq[72][25] = 161;
         * EUC_TWFreq[82][56] = 160; EUC_TWFreq[76][92] = 159; EUC_TWFreq[51][22] = 158; EUC_TWFreq[55][69] = 157;
         * EUC_TWFreq[49][43] = 156; EUC_TWFreq[69][49] = 155; EUC_TWFreq[88][42] = 154; EUC_TWFreq[84][41] = 153;
         * EUC_TWFreq[79][33] = 152; EUC_TWFreq[47][17] = 151; EUC_TWFreq[52][88] = 150; EUC_TWFreq[63][74] = 149;
         * EUC_TWFreq[50][32] = 148; EUC_TWFreq[65][10] = 147; EUC_TWFreq[57][6] = 146; EUC_TWFreq[52][23] = 145;
         * EUC_TWFreq[36][70] = 144; EUC_TWFreq[65][55] = 143; EUC_TWFreq[35][27] = 142; EUC_TWFreq[57][63] = 141;
         * EUC_TWFreq[39][92] = 140; EUC_TWFreq[79][75] = 139; EUC_TWFreq[36][30] = 138; EUC_TWFreq[53][60] = 137;
         * EUC_TWFreq[55][43] = 136; EUC_TWFreq[71][22] = 135; EUC_TWFreq[43][16] = 134; EUC_TWFreq[65][21] = 133;
         * EUC_TWFreq[84][51] = 132; EUC_TWFreq[43][64] = 131; EUC_TWFreq[87][91] = 130; EUC_TWFreq[47][45] = 129;
         * EUC_TWFreq[65][29] = 128; EUC_TWFreq[88][16] = 127; EUC_TWFreq[50][5] = 126; EUC_TWFreq[47][33] = 125;
         * EUC_TWFreq[46][27] = 124; EUC_TWFreq[85][2] = 123; EUC_TWFreq[43][77] = 122; EUC_TWFreq[70][9] = 121;
         * EUC_TWFreq[41][54] = 120; EUC_TWFreq[56][12] = 119; EUC_TWFreq[90][65] = 118; EUC_TWFreq[91][50] = 117;
         * EUC_TWFreq[48][41] = 116; EUC_TWFreq[35][89] = 115; EUC_TWFreq[90][83] = 114; EUC_TWFreq[44][40] = 113;
         * EUC_TWFreq[50][88] = 112; EUC_TWFreq[72][39] = 111; EUC_TWFreq[45][3] = 110; EUC_TWFreq[71][33] = 109;
         * EUC_TWFreq[39][12] = 108; EUC_TWFreq[59][24] = 107; EUC_TWFreq[60][62] = 106; EUC_TWFreq[44][33] = 105;
         * EUC_TWFreq[53][70] = 104; EUC_TWFreq[77][90] = 103; EUC_TWFreq[50][58] = 102; EUC_TWFreq[54][1] = 101;
         * EUC_TWFreq[73][19] = 100; EUC_TWFreq[37][3] = 99; EUC_TWFreq[49][91] = 98; EUC_TWFreq[88][43] = 97;
         * EUC_TWFreq[36][78] = 96; EUC_TWFreq[44][20] = 95; EUC_TWFreq[64][15] = 94; EUC_TWFreq[72][28] = 93;
         * EUC_TWFreq[70][13] = 92; EUC_TWFreq[65][83] = 91; EUC_TWFreq[58][68] = 90; EUC_TWFreq[59][32] = 89;
         * EUC_TWFreq[39][13] = 88; EUC_TWFreq[55][64] = 87; EUC_TWFreq[56][59] = 86; EUC_TWFreq[39][17] = 85;
         * EUC_TWFreq[55][84] = 84; EUC_TWFreq[77][85] = 83; EUC_TWFreq[60][19] = 82; EUC_TWFreq[62][82] = 81;
         * EUC_TWFreq[78][16] = 80; EUC_TWFreq[66][8] = 79; EUC_TWFreq[39][42] = 78; EUC_TWFreq[61][24] = 77;
         * EUC_TWFreq[57][67] = 76; EUC_TWFreq[38][83] = 75; EUC_TWFreq[36][53] = 74; EUC_TWFreq[67][76] = 73;
         * EUC_TWFreq[37][91] = 72; EUC_TWFreq[44][26] = 71; EUC_TWFreq[72][86] = 70; EUC_TWFreq[44][87] = 69;
         * EUC_TWFreq[45][50] = 68; EUC_TWFreq[58][4] = 67; EUC_TWFreq[86][65] = 66; EUC_TWFreq[45][56] = 65;
         * EUC_TWFreq[79][49] = 64; EUC_TWFreq[35][3] = 63; EUC_TWFreq[48][83] = 62; EUC_TWFreq[71][21] = 61;
         * EUC_TWFreq[77][93] = 60; EUC_TWFreq[87][92] = 59; EUC_TWFreq[38][35] = 58; EUC_TWFreq[66][17] = 57;
         * EUC_TWFreq[37][66] = 56; EUC_TWFreq[51][42] = 55; EUC_TWFreq[57][73] = 54; EUC_TWFreq[51][54] = 53;
         * EUC_TWFreq[75][64] = 52; EUC_TWFreq[35][5] = 51; EUC_TWFreq[49][40] = 50; EUC_TWFreq[58][35] = 49;
         * EUC_TWFreq[67][88] = 48; EUC_TWFreq[60][51] = 47; EUC_TWFreq[36][92] = 46; EUC_TWFreq[44][41] = 45;
         * EUC_TWFreq[58][29] = 44; EUC_TWFreq[43][62] = 43; EUC_TWFreq[56][23] = 42; EUC_TWFreq[67][44] = 41;
         * EUC_TWFreq[52][91] = 40; EUC_TWFreq[42][81] = 39; EUC_TWFreq[64][25] = 38; EUC_TWFreq[35][36] = 37;
         * EUC_TWFreq[47][73] = 36; EUC_TWFreq[36][1] = 35; EUC_TWFreq[65][84] = 34; EUC_TWFreq[73][1] = 33;
         * EUC_TWFreq[79][66] = 32; EUC_TWFreq[69][14] = 31; EUC_TWFreq[65][28] = 30; EUC_TWFreq[60][93] = 29;
         * EUC_TWFreq[72][79] = 28; EUC_TWFreq[48][0] = 27; EUC_TWFreq[73][43] = 26; EUC_TWFreq[66][47] = 25;
         * EUC_TWFreq[41][18] = 24; EUC_TWFreq[51][10] = 23; EUC_TWFreq[59][7] = 22; EUC_TWFreq[53][27] = 21;
         * EUC_TWFreq[86][67] = 20; EUC_TWFreq[49][87] = 19; EUC_TWFreq[52][28] = 18; EUC_TWFreq[52][12] = 17;
         * EUC_TWFreq[42][30] = 16; EUC_TWFreq[65][35] = 15; EUC_TWFreq[46][64] = 14; EUC_TWFreq[71][7] = 13;
         * EUC_TWFreq[56][57] = 12; EUC_TWFreq[56][31] = 11; EUC_TWFreq[41][31] = 10; EUC_TWFreq[48][59] = 9;
         * EUC_TWFreq[63][92] = 8; EUC_TWFreq[62][57] = 7; EUC_TWFreq[65][87] = 6; EUC_TWFreq[70][10] = 5;
         * EUC_TWFreq[52][40] = 4; EUC_TWFreq[40][22] = 3; EUC_TWFreq[65][91] = 2; EUC_TWFreq[50][25] = 1;
         * EUC_TWFreq[35][84] = 0;
         */
    }

    /**
     * 初始化 gbkFreq
     */
    void initialize_gbkFreq() {
        gbkFreq[52][132] = 600;
        gbkFreq[73][135] = 599;
        gbkFreq[49][123] = 598;
        gbkFreq[77][146] = 597;
        gbkFreq[81][123] = 596;
        gbkFreq[82][144] = 595;
        gbkFreq[51][179] = 594;
        gbkFreq[83][154] = 593;
        gbkFreq[71][139] = 592;
        gbkFreq[64][139] = 591;
        gbkFreq[85][144] = 590;
        gbkFreq[52][125] = 589;
        gbkFreq[88][25] = 588;
        gbkFreq[81][106] = 587;
        gbkFreq[81][148] = 586;
        gbkFreq[62][137] = 585;
        gbkFreq[94][0] = 584;
        gbkFreq[1][64] = 583;
        gbkFreq[67][163] = 582;
        gbkFreq[20][190] = 581;
        gbkFreq[57][131] = 580;
        gbkFreq[29][169] = 579;
        gbkFreq[72][143] = 578;
        gbkFreq[0][173] = 577;
        gbkFreq[11][23] = 576;
        gbkFreq[61][141] = 575;
        gbkFreq[60][123] = 574;
        gbkFreq[81][114] = 573;
        gbkFreq[82][131] = 572;
        gbkFreq[67][156] = 571;
        gbkFreq[71][167] = 570;
        gbkFreq[20][50] = 569;
        gbkFreq[77][132] = 568;
        gbkFreq[84][38] = 567;
        gbkFreq[26][29] = 566;
        gbkFreq[74][187] = 565;
        gbkFreq[62][116] = 564;
        gbkFreq[67][135] = 563;
        gbkFreq[5][86] = 562;
        gbkFreq[72][186] = 561;
        gbkFreq[75][161] = 560;
        gbkFreq[78][130] = 559;
        gbkFreq[94][30] = 558;
        gbkFreq[84][72] = 557;
        gbkFreq[1][67] = 556;
        gbkFreq[75][172] = 555;
        gbkFreq[74][185] = 554;
        gbkFreq[53][160] = 553;
        gbkFreq[123][14] = 552;
        gbkFreq[79][97] = 551;
        gbkFreq[85][110] = 550;
        gbkFreq[78][171] = 549;
        gbkFreq[52][131] = 548;
        gbkFreq[56][100] = 547;
        gbkFreq[50][182] = 546;
        gbkFreq[94][64] = 545;
        gbkFreq[106][74] = 544;
        gbkFreq[11][102] = 543;
        gbkFreq[53][124] = 542;
        gbkFreq[24][3] = 541;
        gbkFreq[86][148] = 540;
        gbkFreq[53][184] = 539;
        gbkFreq[86][147] = 538;
        gbkFreq[96][161] = 537;
        gbkFreq[82][77] = 536;
        gbkFreq[59][146] = 535;
        gbkFreq[84][126] = 534;
        gbkFreq[79][132] = 533;
        gbkFreq[85][123] = 532;
        gbkFreq[71][101] = 531;
        gbkFreq[85][106] = 530;
        gbkFreq[6][184] = 529;
        gbkFreq[57][156] = 528;
        gbkFreq[75][104] = 527;
        gbkFreq[50][137] = 526;
        gbkFreq[79][133] = 525;
        gbkFreq[76][108] = 524;
        gbkFreq[57][142] = 523;
        gbkFreq[84][130] = 522;
        gbkFreq[52][128] = 521;
        gbkFreq[47][44] = 520;
        gbkFreq[52][152] = 519;
        gbkFreq[54][104] = 518;
        gbkFreq[30][47] = 517;
        gbkFreq[71][123] = 516;
        gbkFreq[52][107] = 515;
        gbkFreq[45][84] = 514;
        gbkFreq[107][118] = 513;
        gbkFreq[5][161] = 512;
        gbkFreq[48][126] = 511;
        gbkFreq[67][170] = 510;
        gbkFreq[43][6] = 509;
        gbkFreq[70][112] = 508;
        gbkFreq[86][174] = 507;
        gbkFreq[84][166] = 506;
        gbkFreq[79][130] = 505;
        gbkFreq[57][141] = 504;
        gbkFreq[81][178] = 503;
        gbkFreq[56][187] = 502;
        gbkFreq[81][162] = 501;
        gbkFreq[53][104] = 500;
        gbkFreq[123][35] = 499;
        gbkFreq[70][169] = 498;
        gbkFreq[69][164] = 497;
        gbkFreq[109][61] = 496;
        gbkFreq[73][130] = 495;
        gbkFreq[62][134] = 494;
        gbkFreq[54][125] = 493;
        gbkFreq[79][105] = 492;
        gbkFreq[70][165] = 491;
        gbkFreq[71][189] = 490;
        gbkFreq[23][147] = 489;
        gbkFreq[51][139] = 488;
        gbkFreq[47][137] = 487;
        gbkFreq[77][123] = 486;
        gbkFreq[86][183] = 485;
        gbkFreq[63][173] = 484;
        gbkFreq[79][144] = 483;
        gbkFreq[84][159] = 482;
        gbkFreq[60][91] = 481;
        gbkFreq[66][187] = 480;
        gbkFreq[73][114] = 479;
        gbkFreq[85][56] = 478;
        gbkFreq[71][149] = 477;
        gbkFreq[84][189] = 476;
        gbkFreq[104][31] = 475;
        gbkFreq[83][82] = 474;
        gbkFreq[68][35] = 473;
        gbkFreq[11][77] = 472;
        gbkFreq[15][155] = 471;
        gbkFreq[83][153] = 470;
        gbkFreq[71][1] = 469;
        gbkFreq[53][190] = 468;
        gbkFreq[50][135] = 467;
        gbkFreq[3][147] = 466;
        gbkFreq[48][136] = 465;
        gbkFreq[66][166] = 464;
        gbkFreq[55][159] = 463;
        gbkFreq[82][150] = 462;
        gbkFreq[58][178] = 461;
        gbkFreq[64][102] = 460;
        gbkFreq[16][106] = 459;
        gbkFreq[68][110] = 458;
        gbkFreq[54][14] = 457;
        gbkFreq[60][140] = 456;
        gbkFreq[91][71] = 455;
        gbkFreq[54][150] = 454;
        gbkFreq[78][177] = 453;
        gbkFreq[78][117] = 452;
        gbkFreq[104][12] = 451;
        gbkFreq[73][150] = 450;
        gbkFreq[51][142] = 449;
        gbkFreq[81][145] = 448;
        gbkFreq[66][183] = 447;
        gbkFreq[51][178] = 446;
        gbkFreq[75][107] = 445;
        gbkFreq[65][119] = 444;
        gbkFreq[69][176] = 443;
        gbkFreq[59][122] = 442;
        gbkFreq[78][160] = 441;
        gbkFreq[85][183] = 440;
        gbkFreq[105][16] = 439;
        gbkFreq[73][110] = 438;
        gbkFreq[104][39] = 437;
        gbkFreq[119][16] = 436;
        gbkFreq[76][162] = 435;
        gbkFreq[67][152] = 434;
        gbkFreq[82][24] = 433;
        gbkFreq[73][121] = 432;
        gbkFreq[83][83] = 431;
        gbkFreq[82][145] = 430;
        gbkFreq[49][133] = 429;
        gbkFreq[94][13] = 428;
        gbkFreq[58][139] = 427;
        gbkFreq[74][189] = 426;
        gbkFreq[66][177] = 425;
        gbkFreq[85][184] = 424;
        gbkFreq[55][183] = 423;
        gbkFreq[71][107] = 422;
        gbkFreq[11][98] = 421;
        gbkFreq[72][153] = 420;
        gbkFreq[2][137] = 419;
        gbkFreq[59][147] = 418;
        gbkFreq[58][152] = 417;
        gbkFreq[55][144] = 416;
        gbkFreq[73][125] = 415;
        gbkFreq[52][154] = 414;
        gbkFreq[70][178] = 413;
        gbkFreq[79][148] = 412;
        gbkFreq[63][143] = 411;
        gbkFreq[50][140] = 410;
        gbkFreq[47][145] = 409;
        gbkFreq[48][123] = 408;
        gbkFreq[56][107] = 407;
        gbkFreq[84][83] = 406;
        gbkFreq[59][112] = 405;
        gbkFreq[124][72] = 404;
        gbkFreq[79][99] = 403;
        gbkFreq[3][37] = 402;
        gbkFreq[114][55] = 401;
        gbkFreq[85][152] = 400;
        gbkFreq[60][47] = 399;
        gbkFreq[65][96] = 398;
        gbkFreq[74][110] = 397;
        gbkFreq[86][182] = 396;
        gbkFreq[50][99] = 395;
        gbkFreq[67][186] = 394;
        gbkFreq[81][74] = 393;
        gbkFreq[80][37] = 392;
        gbkFreq[21][60] = 391;
        gbkFreq[110][12] = 390;
        gbkFreq[60][162] = 389;
        gbkFreq[29][115] = 388;
        gbkFreq[83][130] = 387;
        gbkFreq[52][136] = 386;
        gbkFreq[63][114] = 385;
        gbkFreq[49][127] = 384;
        gbkFreq[83][109] = 383;
        gbkFreq[66][128] = 382;
        gbkFreq[78][136] = 381;
        gbkFreq[81][180] = 380;
        gbkFreq[76][104] = 379;
        gbkFreq[56][156] = 378;
        gbkFreq[61][23] = 377;
        gbkFreq[4][30] = 376;
        gbkFreq[69][154] = 375;
        gbkFreq[100][37] = 374;
        gbkFreq[54][177] = 373;
        gbkFreq[23][119] = 372;
        gbkFreq[71][171] = 371;
        gbkFreq[84][146] = 370;
        gbkFreq[20][184] = 369;
        gbkFreq[86][76] = 368;
        gbkFreq[74][132] = 367;
        gbkFreq[47][97] = 366;
        gbkFreq[82][137] = 365;
        gbkFreq[94][56] = 364;
        gbkFreq[92][30] = 363;
        gbkFreq[19][117] = 362;
        gbkFreq[48][173] = 361;
        gbkFreq[2][136] = 360;
        gbkFreq[7][182] = 359;
        gbkFreq[74][188] = 358;
        gbkFreq[14][132] = 357;
        gbkFreq[62][172] = 356;
        gbkFreq[25][39] = 355;
        gbkFreq[85][129] = 354;
        gbkFreq[64][98] = 353;
        gbkFreq[67][127] = 352;
        gbkFreq[72][167] = 351;
        gbkFreq[57][143] = 350;
        gbkFreq[76][187] = 349;
        gbkFreq[83][181] = 348;
        gbkFreq[84][10] = 347;
        gbkFreq[55][166] = 346;
        gbkFreq[55][188] = 345;
        gbkFreq[13][151] = 344;
        gbkFreq[62][124] = 343;
        gbkFreq[53][136] = 342;
        gbkFreq[106][57] = 341;
        gbkFreq[47][166] = 340;
        gbkFreq[109][30] = 339;
        gbkFreq[78][114] = 338;
        gbkFreq[83][19] = 337;
        gbkFreq[56][162] = 336;
        gbkFreq[60][177] = 335;
        gbkFreq[88][9] = 334;
        gbkFreq[74][163] = 333;
        gbkFreq[52][156] = 332;
        gbkFreq[71][180] = 331;
        gbkFreq[60][57] = 330;
        gbkFreq[72][173] = 329;
        gbkFreq[82][91] = 328;
        gbkFreq[51][186] = 327;
        gbkFreq[75][86] = 326;
        gbkFreq[75][78] = 325;
        gbkFreq[76][170] = 324;
        gbkFreq[60][147] = 323;
        gbkFreq[82][75] = 322;
        gbkFreq[80][148] = 321;
        gbkFreq[86][150] = 320;
        gbkFreq[13][95] = 319;
        gbkFreq[0][11] = 318;
        gbkFreq[84][190] = 317;
        gbkFreq[76][166] = 316;
        gbkFreq[14][72] = 315;
        gbkFreq[67][144] = 314;
        gbkFreq[84][44] = 313;
        gbkFreq[72][125] = 312;
        gbkFreq[66][127] = 311;
        gbkFreq[60][25] = 310;
        gbkFreq[70][146] = 309;
        gbkFreq[79][135] = 308;
        gbkFreq[54][135] = 307;
        gbkFreq[60][104] = 306;
        gbkFreq[55][132] = 305;
        gbkFreq[94][2] = 304;
        gbkFreq[54][133] = 303;
        gbkFreq[56][190] = 302;
        gbkFreq[58][174] = 301;
        gbkFreq[80][144] = 300;
        gbkFreq[85][113] = 299;
        /*
         * GBKFreq[83][15] = 298; GBKFreq[105][80] = 297; GBKFreq[7][179] = 296; GBKFreq[93][4] = 295;
         * GBKFreq[123][40] = 294; GBKFreq[85][120] = 293; GBKFreq[77][165] = 292; GBKFreq[86][67] = 291;
         * GBKFreq[25][162] = 290; GBKFreq[77][183] = 289; GBKFreq[83][71] = 288; GBKFreq[78][99] = 287;
         * GBKFreq[72][177] = 286; GBKFreq[71][97] = 285; GBKFreq[58][111] = 284; GBKFreq[77][175] = 283;
         * GBKFreq[76][181] = 282; GBKFreq[71][142] = 281; GBKFreq[64][150] = 280; GBKFreq[5][142] = 279;
         * GBKFreq[73][128] = 278; GBKFreq[73][156] = 277; GBKFreq[60][188] = 276; GBKFreq[64][56] = 275;
         * GBKFreq[74][128] = 274; GBKFreq[48][163] = 273; GBKFreq[54][116] = 272; GBKFreq[73][127] = 271;
         * GBKFreq[16][176] = 270; GBKFreq[62][149] = 269; GBKFreq[105][96] = 268; GBKFreq[55][186] = 267;
         * GBKFreq[4][51] = 266; GBKFreq[48][113] = 265; GBKFreq[48][152] = 264; GBKFreq[23][9] = 263;
         * GBKFreq[56][102] = 262; GBKFreq[11][81] = 261; GBKFreq[82][112] = 260; GBKFreq[65][85] = 259;
         * GBKFreq[69][125] = 258; GBKFreq[68][31] = 257; GBKFreq[5][20] = 256; GBKFreq[60][176] = 255;
         * GBKFreq[82][81] = 254; GBKFreq[72][107] = 253; GBKFreq[3][52] = 252; GBKFreq[71][157] = 251;
         * GBKFreq[24][46] = 250; GBKFreq[69][108] = 249; GBKFreq[78][178] = 248; GBKFreq[9][69] = 247;
         * GBKFreq[73][144] = 246; GBKFreq[63][187] = 245; GBKFreq[68][36] = 244; GBKFreq[47][151] = 243;
         * GBKFreq[14][74] = 242; GBKFreq[47][114] = 241; GBKFreq[80][171] = 240; GBKFreq[75][152] = 239;
         * GBKFreq[86][40] = 238; GBKFreq[93][43] = 237; GBKFreq[2][50] = 236; GBKFreq[62][66] = 235;
         * GBKFreq[1][183] = 234; GBKFreq[74][124] = 233; GBKFreq[58][104] = 232; GBKFreq[83][106] = 231;
         * GBKFreq[60][144] = 230; GBKFreq[48][99] = 229; GBKFreq[54][157] = 228; GBKFreq[70][179] = 227;
         * GBKFreq[61][127] = 226; GBKFreq[57][135] = 225; GBKFreq[59][190] = 224; GBKFreq[77][116] = 223;
         * GBKFreq[26][17] = 222; GBKFreq[60][13] = 221; GBKFreq[71][38] = 220; GBKFreq[85][177] = 219;
         * GBKFreq[59][73] = 218; GBKFreq[50][150] = 217; GBKFreq[79][102] = 216; GBKFreq[76][118] = 215;
         * GBKFreq[67][132] = 214; GBKFreq[73][146] = 213; GBKFreq[83][184] = 212; GBKFreq[86][159] = 211;
         * GBKFreq[95][120] = 210; GBKFreq[23][139] = 209; GBKFreq[64][183] = 208; GBKFreq[85][103] = 207;
         * GBKFreq[41][90] = 206; GBKFreq[87][72] = 205; GBKFreq[62][104] = 204; GBKFreq[79][168] = 203;
         * GBKFreq[79][150] = 202; GBKFreq[104][20] = 201; GBKFreq[56][114] = 200; GBKFreq[84][26] = 199;
         * GBKFreq[57][99] = 198; GBKFreq[62][154] = 197; GBKFreq[47][98] = 196; GBKFreq[61][64] = 195;
         * GBKFreq[112][18] = 194; GBKFreq[123][19] = 193; GBKFreq[4][98] = 192; GBKFreq[47][163] = 191;
         * GBKFreq[66][188] = 190; GBKFreq[81][85] = 189; GBKFreq[82][30] = 188; GBKFreq[65][83] = 187;
         * GBKFreq[67][24] = 186; GBKFreq[68][179] = 185; GBKFreq[55][177] = 184; GBKFreq[2][122] = 183;
         * GBKFreq[47][139] = 182; GBKFreq[79][158] = 181; GBKFreq[64][143] = 180; GBKFreq[100][24] = 179;
         * GBKFreq[73][103] = 178; GBKFreq[50][148] = 177; GBKFreq[86][97] = 176; GBKFreq[59][116] = 175;
         * GBKFreq[64][173] = 174; GBKFreq[99][91] = 173; GBKFreq[11][99] = 172; GBKFreq[78][179] = 171;
         * GBKFreq[18][17] = 170; GBKFreq[58][185] = 169; GBKFreq[47][165] = 168; GBKFreq[67][131] = 167;
         * GBKFreq[94][40] = 166; GBKFreq[74][153] = 165; GBKFreq[79][142] = 164; GBKFreq[57][98] = 163;
         * GBKFreq[1][164] = 162; GBKFreq[55][168] = 161; GBKFreq[13][141] = 160; GBKFreq[51][31] = 159;
         * GBKFreq[57][178] = 158; GBKFreq[50][189] = 157; GBKFreq[60][167] = 156; GBKFreq[80][34] = 155;
         * GBKFreq[109][80] = 154; GBKFreq[85][54] = 153; GBKFreq[69][183] = 152; GBKFreq[67][143] = 151;
         * GBKFreq[47][120] = 150; GBKFreq[45][75] = 149; GBKFreq[82][98] = 148; GBKFreq[83][22] = 147;
         * GBKFreq[13][103] = 146; GBKFreq[49][174] = 145; GBKFreq[57][181] = 144; GBKFreq[64][127] = 143;
         * GBKFreq[61][131] = 142; GBKFreq[52][180] = 141; GBKFreq[74][134] = 140; GBKFreq[84][187] = 139;
         * GBKFreq[81][189] = 138; GBKFreq[47][160] = 137; GBKFreq[66][148] = 136; GBKFreq[7][4] = 135;
         * GBKFreq[85][134] = 134; GBKFreq[88][13] = 133; GBKFreq[88][80] = 132; GBKFreq[69][166] = 131;
         * GBKFreq[86][18] = 130; GBKFreq[79][141] = 129; GBKFreq[50][108] = 128; GBKFreq[94][69] = 127;
         * GBKFreq[81][110] = 126; GBKFreq[69][119] = 125; GBKFreq[72][161] = 124; GBKFreq[106][45] = 123;
         * GBKFreq[73][124] = 122; GBKFreq[94][28] = 121; GBKFreq[63][174] = 120; GBKFreq[3][149] = 119;
         * GBKFreq[24][160] = 118; GBKFreq[113][94] = 117; GBKFreq[56][138] = 116; GBKFreq[64][185] = 115;
         * GBKFreq[86][56] = 114; GBKFreq[56][150] = 113; GBKFreq[110][55] = 112; GBKFreq[28][13] = 111;
         * GBKFreq[54][190] = 110; GBKFreq[8][180] = 109; GBKFreq[73][149] = 108; GBKFreq[80][155] = 107;
         * GBKFreq[83][172] = 106; GBKFreq[67][174] = 105; GBKFreq[64][180] = 104; GBKFreq[84][46] = 103;
         * GBKFreq[91][74] = 102; GBKFreq[69][134] = 101; GBKFreq[61][107] = 100; GBKFreq[47][171] = 99;
         * GBKFreq[59][51] = 98; GBKFreq[109][74] = 97; GBKFreq[64][174] = 96; GBKFreq[52][151] = 95;
         * GBKFreq[51][176] = 94; GBKFreq[80][157] = 93; GBKFreq[94][31] = 92; GBKFreq[79][155] = 91;
         * GBKFreq[72][174] = 90; GBKFreq[69][113] = 89; GBKFreq[83][167] = 88; GBKFreq[83][122] = 87;
         * GBKFreq[8][178] = 86; GBKFreq[70][186] = 85; GBKFreq[59][153] = 84; GBKFreq[84][68] = 83; GBKFreq[79][39]
         * = 82; GBKFreq[47][180] = 81; GBKFreq[88][53] = 80; GBKFreq[57][154] = 79; GBKFreq[47][153] = 78;
         * GBKFreq[3][153] = 77; GBKFreq[76][134] = 76; GBKFreq[51][166] = 75; GBKFreq[58][176] = 74;
         * GBKFreq[27][138] = 73; GBKFreq[73][126] = 72; GBKFreq[76][185] = 71; GBKFreq[52][186] = 70;
         * GBKFreq[81][151] = 69; GBKFreq[26][50] = 68; GBKFreq[76][173] = 67; GBKFreq[106][56] = 66;
         * GBKFreq[85][142] = 65; GBKFreq[11][103] = 64; GBKFreq[69][159] = 63; GBKFreq[53][142] = 62; GBKFreq[7][6]
         * = 61; GBKFreq[84][59] = 60; GBKFreq[86][3] = 59; GBKFreq[64][144] = 58; GBKFreq[1][187] = 57;
         * GBKFreq[82][128] = 56; GBKFreq[3][66] = 55; GBKFreq[68][133] = 54; GBKFreq[55][167] = 53;
         * GBKFreq[52][130] = 52; GBKFreq[61][133] = 51; GBKFreq[72][181] = 50; GBKFreq[25][98] = 49;
         * GBKFreq[84][149] = 48; GBKFreq[91][91] = 47; GBKFreq[47][188] = 46; GBKFreq[68][130] = 45;
         * GBKFreq[22][44] = 44; GBKFreq[81][121] = 43; GBKFreq[72][140] = 42; GBKFreq[55][133] = 41;
         * GBKFreq[55][185] = 40; GBKFreq[56][105] = 39; GBKFreq[60][30] = 38; GBKFreq[70][103] = 37;
         * GBKFreq[62][141] = 36; GBKFreq[70][144] = 35; GBKFreq[59][111] = 34; GBKFreq[54][17] = 33;
         * GBKFreq[18][190] = 32; GBKFreq[65][164] = 31; GBKFreq[83][125] = 30; GBKFreq[61][121] = 29;
         * GBKFreq[48][13] = 28; GBKFreq[51][189] = 27; GBKFreq[65][68] = 26; GBKFreq[7][0] = 25; GBKFreq[76][188] =
         * 24; GBKFreq[85][117] = 23; GBKFreq[45][33] = 22; GBKFreq[78][187] = 21; GBKFreq[106][48] = 20;
         * GBKFreq[59][52] = 19; GBKFreq[86][185] = 18; GBKFreq[84][121] = 17; GBKFreq[82][189] = 16;
         * GBKFreq[68][156] = 15; GBKFreq[55][125] = 14; GBKFreq[65][175] = 13; GBKFreq[7][140] = 12;
         * GBKFreq[50][106] = 11; GBKFreq[59][124] = 10; GBKFreq[67][115] = 9; GBKFreq[82][114] = 8;
         * GBKFreq[74][121] = 7; GBKFreq[106][69] = 6; GBKFreq[94][27] = 5; GBKFreq[78][98] = 4; GBKFreq[85][186] =
         * 3; GBKFreq[108][90] = 2; GBKFreq[62][160] = 1; GBKFreq[60][169] = 0;
         */
    }

    /**
     * 初始化 krFreq
     */
    void initialize_krFreq() {
        krFreq[31][43] = 600;
        krFreq[19][56] = 599;
        krFreq[38][46] = 598;
        krFreq[3][3] = 597;
        krFreq[29][77] = 596;
        krFreq[19][33] = 595;
        krFreq[30][0] = 594;
        krFreq[29][89] = 593;
        krFreq[31][26] = 592;
        krFreq[31][38] = 591;
        krFreq[32][85] = 590;
        krFreq[15][0] = 589;
        krFreq[16][54] = 588;
        krFreq[15][76] = 587;
        krFreq[31][25] = 586;
        krFreq[23][13] = 585;
        krFreq[28][34] = 584;
        krFreq[18][9] = 583;
        krFreq[29][37] = 582;
        krFreq[22][45] = 581;
        krFreq[19][46] = 580;
        krFreq[16][65] = 579;
        krFreq[23][5] = 578;
        krFreq[26][70] = 577;
        krFreq[31][53] = 576;
        krFreq[27][12] = 575;
        krFreq[30][67] = 574;
        krFreq[31][57] = 573;
        krFreq[20][20] = 572;
        krFreq[30][31] = 571;
        krFreq[20][72] = 570;
        krFreq[15][51] = 569;
        krFreq[3][8] = 568;
        krFreq[32][53] = 567;
        krFreq[27][85] = 566;
        krFreq[25][23] = 565;
        krFreq[15][44] = 564;
        krFreq[32][3] = 563;
        krFreq[31][68] = 562;
        krFreq[30][24] = 561;
        krFreq[29][49] = 560;
        krFreq[27][49] = 559;
        krFreq[23][23] = 558;
        krFreq[31][91] = 557;
        krFreq[31][46] = 556;
        krFreq[19][74] = 555;
        krFreq[27][27] = 554;
        krFreq[3][17] = 553;
        krFreq[20][38] = 552;
        krFreq[21][82] = 551;
        krFreq[28][25] = 550;
        krFreq[32][5] = 549;
        krFreq[31][23] = 548;
        krFreq[25][45] = 547;
        krFreq[32][87] = 546;
        krFreq[18][26] = 545;
        krFreq[24][10] = 544;
        krFreq[26][82] = 543;
        krFreq[15][89] = 542;
        krFreq[28][36] = 541;
        krFreq[28][31] = 540;
        krFreq[16][23] = 539;
        krFreq[16][77] = 538;
        krFreq[19][84] = 537;
        krFreq[23][72] = 536;
        krFreq[38][48] = 535;
        krFreq[23][2] = 534;
        krFreq[30][20] = 533;
        krFreq[38][47] = 532;
        krFreq[39][12] = 531;
        krFreq[23][21] = 530;
        krFreq[18][17] = 529;
        krFreq[30][87] = 528;
        krFreq[29][62] = 527;
        krFreq[29][87] = 526;
        krFreq[34][53] = 525;
        krFreq[32][29] = 524;
        krFreq[35][0] = 523;
        krFreq[24][43] = 522;
        krFreq[36][44] = 521;
        krFreq[20][30] = 520;
        krFreq[39][86] = 519;
        krFreq[22][14] = 518;
        krFreq[29][39] = 517;
        krFreq[28][38] = 516;
        krFreq[23][79] = 515;
        krFreq[24][56] = 514;
        krFreq[29][63] = 513;
        krFreq[31][45] = 512;
        krFreq[23][26] = 511;
        krFreq[15][87] = 510;
        krFreq[30][74] = 509;
        krFreq[24][69] = 508;
        krFreq[20][4] = 507;
        krFreq[27][50] = 506;
        krFreq[30][75] = 505;
        krFreq[24][13] = 504;
        krFreq[30][8] = 503;
        krFreq[31][6] = 502;
        krFreq[25][80] = 501;
        krFreq[36][8] = 500;
        krFreq[15][18] = 499;
        krFreq[39][23] = 498;
        krFreq[16][24] = 497;
        krFreq[31][89] = 496;
        krFreq[15][71] = 495;
        krFreq[15][57] = 494;
        krFreq[30][11] = 493;
        krFreq[15][36] = 492;
        krFreq[16][60] = 491;
        krFreq[24][45] = 490;
        krFreq[37][35] = 489;
        krFreq[24][87] = 488;
        krFreq[20][45] = 487;
        krFreq[31][90] = 486;
        krFreq[32][21] = 485;
        krFreq[19][70] = 484;
        krFreq[24][15] = 483;
        krFreq[26][92] = 482;
        krFreq[37][13] = 481;
        krFreq[39][2] = 480;
        krFreq[23][70] = 479;
        krFreq[27][25] = 478;
        krFreq[15][69] = 477;
        krFreq[19][61] = 476;
        krFreq[31][58] = 475;
        krFreq[24][57] = 474;
        krFreq[36][74] = 473;
        krFreq[21][6] = 472;
        krFreq[30][44] = 471;
        krFreq[15][91] = 470;
        krFreq[27][16] = 469;
        krFreq[29][42] = 468;
        krFreq[33][86] = 467;
        krFreq[29][41] = 466;
        krFreq[20][68] = 465;
        krFreq[25][47] = 464;
        krFreq[22][0] = 463;
        krFreq[18][14] = 462;
        krFreq[31][28] = 461;
        krFreq[15][2] = 460;
        krFreq[23][76] = 459;
        krFreq[38][32] = 458;
        krFreq[29][82] = 457;
        krFreq[21][86] = 456;
        krFreq[24][62] = 455;
        krFreq[31][64] = 454;
        krFreq[38][26] = 453;
        krFreq[32][86] = 452;
        krFreq[22][32] = 451;
        krFreq[19][59] = 450;
        krFreq[34][18] = 449;
        krFreq[18][54] = 448;
        krFreq[38][63] = 447;
        krFreq[36][23] = 446;
        krFreq[35][35] = 445;
        krFreq[32][62] = 444;
        krFreq[28][35] = 443;
        krFreq[27][13] = 442;
        krFreq[31][59] = 441;
        krFreq[29][29] = 440;
        krFreq[15][64] = 439;
        krFreq[26][84] = 438;
        krFreq[21][90] = 437;
        krFreq[20][24] = 436;
        krFreq[16][18] = 435;
        krFreq[22][23] = 434;
        krFreq[31][14] = 433;
        krFreq[15][1] = 432;
        krFreq[18][63] = 431;
        krFreq[19][10] = 430;
        krFreq[25][49] = 429;
        krFreq[36][57] = 428;
        krFreq[20][22] = 427;
        krFreq[15][15] = 426;
        krFreq[31][51] = 425;
        krFreq[24][60] = 424;
        krFreq[31][70] = 423;
        krFreq[15][7] = 422;
        krFreq[28][40] = 421;
        krFreq[18][41] = 420;
        krFreq[15][38] = 419;
        krFreq[32][0] = 418;
        krFreq[19][51] = 417;
        krFreq[34][62] = 416;
        krFreq[16][27] = 415;
        krFreq[20][70] = 414;
        krFreq[22][33] = 413;
        krFreq[26][73] = 412;
        krFreq[20][79] = 411;
        krFreq[23][6] = 410;
        krFreq[24][85] = 409;
        krFreq[38][51] = 408;
        krFreq[29][88] = 407;
        krFreq[38][55] = 406;
        krFreq[32][32] = 405;
        krFreq[27][18] = 404;
        krFreq[23][87] = 403;
        krFreq[35][6] = 402;
        krFreq[34][27] = 401;
        krFreq[39][35] = 400;
        krFreq[30][88] = 399;
        krFreq[32][92] = 398;
        krFreq[32][49] = 397;
        krFreq[24][61] = 396;
        krFreq[18][74] = 395;
        krFreq[23][77] = 394;
        krFreq[23][50] = 393;
        krFreq[23][32] = 392;
        krFreq[23][36] = 391;
        krFreq[38][38] = 390;
        krFreq[29][86] = 389;
        krFreq[36][15] = 388;
        krFreq[31][50] = 387;
        krFreq[15][86] = 386;
        krFreq[39][13] = 385;
        krFreq[34][26] = 384;
        krFreq[19][34] = 383;
        krFreq[16][3] = 382;
        krFreq[26][93] = 381;
        krFreq[19][67] = 380;
        krFreq[24][72] = 379;
        krFreq[29][17] = 378;
        krFreq[23][24] = 377;
        krFreq[25][19] = 376;
        krFreq[18][65] = 375;
        krFreq[30][78] = 374;
        krFreq[27][52] = 373;
        krFreq[22][18] = 372;
        krFreq[16][38] = 371;
        krFreq[21][26] = 370;
        krFreq[34][20] = 369;
        krFreq[15][42] = 368;
        krFreq[16][71] = 367;
        krFreq[17][17] = 366;
        krFreq[24][71] = 365;
        krFreq[18][84] = 364;
        krFreq[15][40] = 363;
        krFreq[31][62] = 362;
        krFreq[15][8] = 361;
        krFreq[16][69] = 360;
        krFreq[29][79] = 359;
        krFreq[38][91] = 358;
        krFreq[31][92] = 357;
        krFreq[20][77] = 356;
        krFreq[3][16] = 355;
        krFreq[27][87] = 354;
        krFreq[16][25] = 353;
        krFreq[36][33] = 352;
        krFreq[37][76] = 351;
        krFreq[30][12] = 350;
        krFreq[26][75] = 349;
        krFreq[25][14] = 348;
        krFreq[32][26] = 347;
        krFreq[23][22] = 346;
        krFreq[20][90] = 345;
        krFreq[19][8] = 344;
        krFreq[38][41] = 343;
        krFreq[34][2] = 342;
        krFreq[39][4] = 341;
        krFreq[27][89] = 340;
        krFreq[28][41] = 339;
        krFreq[28][44] = 338;
        krFreq[24][92] = 337;
        krFreq[34][65] = 336;
        krFreq[39][14] = 335;
        krFreq[21][38] = 334;
        krFreq[19][31] = 333;
        krFreq[37][39] = 332;
        krFreq[33][41] = 331;
        krFreq[38][4] = 330;
        krFreq[23][80] = 329;
        krFreq[25][24] = 328;
        krFreq[37][17] = 327;
        krFreq[22][16] = 326;
        krFreq[22][46] = 325;
        krFreq[33][91] = 324;
        krFreq[24][89] = 323;
        krFreq[30][52] = 322;
        krFreq[29][38] = 321;
        krFreq[38][85] = 320;
        krFreq[15][12] = 319;
        krFreq[27][58] = 318;
        krFreq[29][52] = 317;
        krFreq[37][38] = 316;
        krFreq[34][41] = 315;
        krFreq[31][65] = 314;
        krFreq[29][53] = 313;
        krFreq[22][47] = 312;
        krFreq[22][19] = 311;
        krFreq[26][0] = 310;
        krFreq[37][86] = 309;
        krFreq[35][4] = 308;
        krFreq[36][54] = 307;
        krFreq[20][76] = 306;
        krFreq[30][9] = 305;
        krFreq[30][33] = 304;
        krFreq[23][17] = 303;
        krFreq[23][33] = 302;
        krFreq[38][52] = 301;
        krFreq[15][19] = 300;
        krFreq[28][45] = 299;
        krFreq[29][78] = 298;
        krFreq[23][15] = 297;
        krFreq[33][5] = 296;
        krFreq[17][40] = 295;
        krFreq[30][83] = 294;
        krFreq[18][1] = 293;
        krFreq[30][81] = 292;
        krFreq[19][40] = 291;
        krFreq[24][47] = 290;
        krFreq[17][56] = 289;
        krFreq[39][80] = 288;
        krFreq[30][46] = 287;
        krFreq[16][61] = 286;
        krFreq[26][78] = 285;
        krFreq[26][57] = 284;
        krFreq[20][46] = 283;
        krFreq[25][15] = 282;
        krFreq[25][91] = 281;
        krFreq[21][83] = 280;
        krFreq[30][77] = 279;
        krFreq[35][30] = 278;
        krFreq[30][34] = 277;
        krFreq[20][69] = 276;
        krFreq[35][10] = 275;
        krFreq[29][70] = 274;
        krFreq[22][50] = 273;
        krFreq[18][0] = 272;
        krFreq[22][64] = 271;
        krFreq[38][65] = 270;
        krFreq[22][70] = 269;
        krFreq[24][58] = 268;
        krFreq[19][66] = 267;
        krFreq[30][59] = 266;
        krFreq[37][14] = 265;
        krFreq[16][56] = 264;
        krFreq[29][85] = 263;
        krFreq[31][15] = 262;
        krFreq[36][84] = 261;
        krFreq[39][15] = 260;
        krFreq[39][90] = 259;
        krFreq[18][12] = 258;
        krFreq[21][93] = 257;
        krFreq[24][66] = 256;
        krFreq[27][90] = 255;
        krFreq[25][90] = 254;
        krFreq[22][24] = 253;
        krFreq[36][67] = 252;
        krFreq[33][90] = 251;
        krFreq[15][60] = 250;
        krFreq[23][85] = 249;
        krFreq[34][1] = 248;
        krFreq[39][37] = 247;
        krFreq[21][18] = 246;
        krFreq[34][4] = 245;
        krFreq[28][33] = 244;
        krFreq[15][13] = 243;
        krFreq[32][22] = 242;
        krFreq[30][76] = 241;
        krFreq[20][21] = 240;
        krFreq[38][66] = 239;
        krFreq[32][55] = 238;
        krFreq[32][89] = 237;
        krFreq[25][26] = 236;
        krFreq[16][80] = 235;
        krFreq[15][43] = 234;
        krFreq[38][54] = 233;
        krFreq[39][68] = 232;
        krFreq[22][88] = 231;
        krFreq[21][84] = 230;
        krFreq[21][17] = 229;
        krFreq[20][28] = 228;
        krFreq[32][1] = 227;
        krFreq[33][87] = 226;
        krFreq[38][71] = 225;
        krFreq[37][47] = 224;
        krFreq[18][77] = 223;
        krFreq[37][58] = 222;
        krFreq[34][74] = 221;
        krFreq[32][54] = 220;
        krFreq[27][33] = 219;
        krFreq[32][93] = 218;
        krFreq[23][51] = 217;
        krFreq[20][57] = 216;
        krFreq[22][37] = 215;
        krFreq[39][10] = 214;
        krFreq[39][17] = 213;
        krFreq[33][4] = 212;
        krFreq[32][84] = 211;
        krFreq[34][3] = 210;
        krFreq[28][27] = 209;
        krFreq[15][79] = 208;
        krFreq[34][21] = 207;
        krFreq[34][69] = 206;
        krFreq[21][62] = 205;
        krFreq[36][24] = 204;
        krFreq[16][89] = 203;
        krFreq[18][48] = 202;
        krFreq[38][15] = 201;
        krFreq[36][58] = 200;
        krFreq[21][56] = 199;
        krFreq[34][48] = 198;
        krFreq[21][15] = 197;
        krFreq[39][3] = 196;
        krFreq[16][44] = 195;
        krFreq[18][79] = 194;
        krFreq[25][13] = 193;
        krFreq[29][47] = 192;
        krFreq[38][88] = 191;
        krFreq[20][71] = 190;
        krFreq[16][58] = 189;
        krFreq[35][57] = 188;
        krFreq[29][30] = 187;
        krFreq[29][23] = 186;
        krFreq[34][93] = 185;
        krFreq[30][85] = 184;
        krFreq[15][80] = 183;
        krFreq[32][78] = 182;
        krFreq[37][82] = 181;
        krFreq[22][40] = 180;
        krFreq[21][69] = 179;
        krFreq[26][85] = 178;
        krFreq[31][31] = 177;
        krFreq[28][64] = 176;
        krFreq[38][13] = 175;
        krFreq[25][2] = 174;
        krFreq[22][34] = 173;
        krFreq[28][28] = 172;
        krFreq[24][91] = 171;
        krFreq[33][74] = 170;
        krFreq[29][40] = 169;
        krFreq[15][77] = 168;
        krFreq[32][80] = 167;
        krFreq[30][41] = 166;
        krFreq[23][30] = 165;
        krFreq[24][63] = 164;
        krFreq[30][53] = 163;
        krFreq[39][70] = 162;
        krFreq[23][61] = 161;
        krFreq[37][27] = 160;
        krFreq[16][55] = 159;
        krFreq[22][74] = 158;
        krFreq[26][50] = 157;
        krFreq[16][10] = 156;
        krFreq[34][63] = 155;
        krFreq[35][14] = 154;
        krFreq[17][7] = 153;
        krFreq[15][59] = 152;
        krFreq[27][23] = 151;
        krFreq[18][70] = 150;
        krFreq[32][56] = 149;
        krFreq[37][87] = 148;
        krFreq[17][61] = 147;
        krFreq[18][83] = 146;
        krFreq[23][86] = 145;
        krFreq[17][31] = 144;
        krFreq[23][83] = 143;
        krFreq[35][2] = 142;
        krFreq[18][64] = 141;
        krFreq[27][43] = 140;
        krFreq[32][42] = 139;
        krFreq[25][76] = 138;
        krFreq[19][85] = 137;
        krFreq[37][81] = 136;
        krFreq[38][83] = 135;
        krFreq[35][7] = 134;
        krFreq[16][51] = 133;
        krFreq[27][22] = 132;
        krFreq[16][76] = 131;
        krFreq[22][4] = 130;
        krFreq[38][84] = 129;
        krFreq[17][83] = 128;
        krFreq[24][46] = 127;
        krFreq[33][15] = 126;
        krFreq[20][48] = 125;
        krFreq[17][30] = 124;
        krFreq[30][93] = 123;
        krFreq[28][11] = 122;
        krFreq[28][30] = 121;
        krFreq[15][62] = 120;
        krFreq[17][87] = 119;
        krFreq[32][81] = 118;
        krFreq[23][37] = 117;
        krFreq[30][22] = 116;
        krFreq[32][66] = 115;
        krFreq[33][78] = 114;
        krFreq[21][4] = 113;
        krFreq[31][17] = 112;
        krFreq[39][61] = 111;
        krFreq[18][76] = 110;
        krFreq[15][85] = 109;
        krFreq[31][47] = 108;
        krFreq[19][57] = 107;
        krFreq[23][55] = 106;
        krFreq[27][29] = 105;
        krFreq[29][46] = 104;
        krFreq[33][0] = 103;
        krFreq[16][83] = 102;
        krFreq[39][78] = 101;
        krFreq[32][77] = 100;
        krFreq[36][25] = 99;
        krFreq[34][19] = 98;
        krFreq[38][49] = 97;
        krFreq[19][25] = 96;
        krFreq[23][53] = 95;
        krFreq[28][43] = 94;
        krFreq[31][44] = 93;
        krFreq[36][34] = 92;
        krFreq[16][34] = 91;
        krFreq[35][1] = 90;
        krFreq[19][87] = 89;
        krFreq[18][53] = 88;
        krFreq[29][54] = 87;
        krFreq[22][41] = 86;
        krFreq[38][18] = 85;
        krFreq[22][2] = 84;
        krFreq[20][3] = 83;
        krFreq[39][69] = 82;
        krFreq[30][29] = 81;
        krFreq[28][19] = 80;
        krFreq[29][90] = 79;
        krFreq[17][86] = 78;
        krFreq[15][9] = 77;
        krFreq[39][73] = 76;
        krFreq[15][37] = 75;
        krFreq[35][40] = 74;
        krFreq[33][77] = 73;
        krFreq[27][86] = 72;
        krFreq[36][79] = 71;
        krFreq[23][18] = 70;
        krFreq[34][87] = 69;
        krFreq[39][24] = 68;
        krFreq[26][8] = 67;
        krFreq[33][48] = 66;
        krFreq[39][30] = 65;
        krFreq[33][28] = 64;
        krFreq[16][67] = 63;
        krFreq[31][78] = 62;
        krFreq[32][23] = 61;
        krFreq[24][55] = 60;
        krFreq[30][68] = 59;
        krFreq[18][60] = 58;
        krFreq[15][17] = 57;
        krFreq[23][34] = 56;
        krFreq[20][49] = 55;
        krFreq[15][78] = 54;
        krFreq[24][14] = 53;
        krFreq[19][41] = 52;
        krFreq[31][55] = 51;
        krFreq[21][39] = 50;
        krFreq[35][9] = 49;
        krFreq[30][15] = 48;
        krFreq[20][52] = 47;
        krFreq[35][71] = 46;
        krFreq[20][7] = 45;
        krFreq[29][72] = 44;
        krFreq[37][77] = 43;
        krFreq[22][35] = 42;
        krFreq[20][61] = 41;
        krFreq[31][60] = 40;
        krFreq[20][93] = 39;
        krFreq[27][92] = 38;
        krFreq[28][16] = 37;
        krFreq[36][26] = 36;
        krFreq[18][89] = 35;
        krFreq[21][63] = 34;
        krFreq[22][52] = 33;
        krFreq[24][65] = 32;
        krFreq[31][8] = 31;
        krFreq[31][49] = 30;
        krFreq[33][30] = 29;
        krFreq[37][15] = 28;
        krFreq[18][18] = 27;
        krFreq[25][50] = 26;
        krFreq[29][20] = 25;
        krFreq[35][48] = 24;
        krFreq[38][75] = 23;
        krFreq[26][83] = 22;
        krFreq[21][87] = 21;
        krFreq[27][71] = 20;
        krFreq[32][91] = 19;
        krFreq[25][73] = 18;
        krFreq[16][84] = 17;
        krFreq[25][31] = 16;
        krFreq[17][90] = 15;
        krFreq[18][40] = 14;
        krFreq[17][77] = 13;
        krFreq[17][35] = 12;
        krFreq[23][52] = 11;
        krFreq[23][35] = 10;
        krFreq[16][5] = 9;
        krFreq[23][58] = 8;
        krFreq[19][60] = 7;
        krFreq[30][32] = 6;
        krFreq[38][34] = 5;
        krFreq[23][4] = 4;
        krFreq[23][1] = 3;
        krFreq[27][57] = 2;
        krFreq[39][38] = 1;
        krFreq[32][33] = 0;
    }

    /**
     * 初始化 jpFreq
     */
    void initialize_jpFreq() {
        jpFreq[3][74] = 600;
        jpFreq[3][45] = 599;
        jpFreq[3][3] = 598;
        jpFreq[3][24] = 597;
        jpFreq[3][30] = 596;
        jpFreq[3][42] = 595;
        jpFreq[3][46] = 594;
        jpFreq[3][39] = 593;
        jpFreq[3][11] = 592;
        jpFreq[3][37] = 591;
        jpFreq[3][38] = 590;
        jpFreq[3][31] = 589;
        jpFreq[3][41] = 588;
        jpFreq[3][5] = 587;
        jpFreq[3][10] = 586;
        jpFreq[3][75] = 585;
        jpFreq[3][65] = 584;
        jpFreq[3][72] = 583;
        jpFreq[37][91] = 582;
        jpFreq[0][27] = 581;
        jpFreq[3][18] = 580;
        jpFreq[3][22] = 579;
        jpFreq[3][61] = 578;
        jpFreq[3][14] = 577;
        jpFreq[24][80] = 576;
        jpFreq[4][82] = 575;
        jpFreq[17][80] = 574;
        jpFreq[30][44] = 573;
        jpFreq[3][73] = 572;
        jpFreq[3][64] = 571;
        jpFreq[38][14] = 570;
        jpFreq[33][70] = 569;
        jpFreq[3][1] = 568;
        jpFreq[3][16] = 567;
        jpFreq[3][35] = 566;
        jpFreq[3][40] = 565;
        jpFreq[4][74] = 564;
        jpFreq[4][24] = 563;
        jpFreq[42][59] = 562;
        jpFreq[3][7] = 561;
        jpFreq[3][71] = 560;
        jpFreq[3][12] = 559;
        jpFreq[15][75] = 558;
        jpFreq[3][20] = 557;
        jpFreq[4][39] = 556;
        jpFreq[34][69] = 555;
        jpFreq[3][28] = 554;
        jpFreq[35][24] = 553;
        jpFreq[3][82] = 552;
        jpFreq[28][47] = 551;
        jpFreq[3][67] = 550;
        jpFreq[37][16] = 549;
        jpFreq[26][93] = 548;
        jpFreq[4][1] = 547;
        jpFreq[26][85] = 546;
        jpFreq[31][14] = 545;
        jpFreq[4][3] = 544;
        jpFreq[4][72] = 543;
        jpFreq[24][51] = 542;
        jpFreq[27][51] = 541;
        jpFreq[27][49] = 540;
        jpFreq[22][77] = 539;
        jpFreq[27][10] = 538;
        jpFreq[29][68] = 537;
        jpFreq[20][35] = 536;
        jpFreq[41][11] = 535;
        jpFreq[24][70] = 534;
        jpFreq[36][61] = 533;
        jpFreq[31][23] = 532;
        jpFreq[43][16] = 531;
        jpFreq[23][68] = 530;
        jpFreq[32][15] = 529;
        jpFreq[3][32] = 528;
        jpFreq[19][53] = 527;
        jpFreq[40][83] = 526;
        jpFreq[4][14] = 525;
        jpFreq[36][9] = 524;
        jpFreq[4][73] = 523;
        jpFreq[23][10] = 522;
        jpFreq[3][63] = 521;
        jpFreq[39][14] = 520;
        jpFreq[3][78] = 519;
        jpFreq[33][47] = 518;
        jpFreq[21][39] = 517;
        jpFreq[34][46] = 516;
        jpFreq[36][75] = 515;
        jpFreq[41][92] = 514;
        jpFreq[37][93] = 513;
        jpFreq[4][34] = 512;
        jpFreq[15][86] = 511;
        jpFreq[46][1] = 510;
        jpFreq[37][65] = 509;
        jpFreq[3][62] = 508;
        jpFreq[32][73] = 507;
        jpFreq[21][65] = 506;
        jpFreq[29][75] = 505;
        jpFreq[26][51] = 504;
        jpFreq[3][34] = 503;
        jpFreq[4][10] = 502;
        jpFreq[30][22] = 501;
        jpFreq[35][73] = 500;
        jpFreq[17][82] = 499;
        jpFreq[45][8] = 498;
        jpFreq[27][73] = 497;
        jpFreq[18][55] = 496;
        jpFreq[25][2] = 495;
        jpFreq[3][26] = 494;
        jpFreq[45][46] = 493;
        jpFreq[4][22] = 492;
        jpFreq[4][40] = 491;
        jpFreq[18][10] = 490;
        jpFreq[32][9] = 489;
        jpFreq[26][49] = 488;
        jpFreq[3][47] = 487;
        jpFreq[24][65] = 486;
        jpFreq[4][76] = 485;
        jpFreq[43][67] = 484;
        jpFreq[3][9] = 483;
        jpFreq[41][37] = 482;
        jpFreq[33][68] = 481;
        jpFreq[43][31] = 480;
        jpFreq[19][55] = 479;
        jpFreq[4][30] = 478;
        jpFreq[27][33] = 477;
        jpFreq[16][62] = 476;
        jpFreq[36][35] = 475;
        jpFreq[37][15] = 474;
        jpFreq[27][70] = 473;
        jpFreq[22][71] = 472;
        jpFreq[33][45] = 471;
        jpFreq[31][78] = 470;
        jpFreq[43][59] = 469;
        jpFreq[32][19] = 468;
        jpFreq[17][28] = 467;
        jpFreq[40][28] = 466;
        jpFreq[20][93] = 465;
        jpFreq[18][15] = 464;
        jpFreq[4][23] = 463;
        jpFreq[3][23] = 462;
        jpFreq[26][64] = 461;
        jpFreq[44][92] = 460;
        jpFreq[17][27] = 459;
        jpFreq[3][56] = 458;
        jpFreq[25][38] = 457;
        jpFreq[23][31] = 456;
        jpFreq[35][43] = 455;
        jpFreq[4][54] = 454;
        jpFreq[35][19] = 453;
        jpFreq[22][47] = 452;
        jpFreq[42][0] = 451;
        jpFreq[23][28] = 450;
        jpFreq[46][33] = 449;
        jpFreq[36][85] = 448;
        jpFreq[31][12] = 447;
        jpFreq[3][76] = 446;
        jpFreq[4][75] = 445;
        jpFreq[36][56] = 444;
        jpFreq[4][64] = 443;
        jpFreq[25][77] = 442;
        jpFreq[15][52] = 441;
        jpFreq[33][73] = 440;
        jpFreq[3][55] = 439;
        jpFreq[43][82] = 438;
        jpFreq[27][82] = 437;
        jpFreq[20][3] = 436;
        jpFreq[40][51] = 435;
        jpFreq[3][17] = 434;
        jpFreq[27][71] = 433;
        jpFreq[4][52] = 432;
        jpFreq[44][48] = 431;
        jpFreq[27][2] = 430;
        jpFreq[17][39] = 429;
        jpFreq[31][8] = 428;
        jpFreq[44][54] = 427;
        jpFreq[43][18] = 426;
        jpFreq[43][77] = 425;
        jpFreq[4][61] = 424;
        jpFreq[19][91] = 423;
        jpFreq[31][13] = 422;
        jpFreq[44][71] = 421;
        jpFreq[20][0] = 420;
        jpFreq[23][87] = 419;
        jpFreq[21][14] = 418;
        jpFreq[29][13] = 417;
        jpFreq[3][58] = 416;
        jpFreq[26][18] = 415;
        jpFreq[4][47] = 414;
        jpFreq[4][18] = 413;
        jpFreq[3][53] = 412;
        jpFreq[26][92] = 411;
        jpFreq[21][7] = 410;
        jpFreq[4][37] = 409;
        jpFreq[4][63] = 408;
        jpFreq[36][51] = 407;
        jpFreq[4][32] = 406;
        jpFreq[28][73] = 405;
        jpFreq[4][50] = 404;
        jpFreq[41][60] = 403;
        jpFreq[23][1] = 402;
        jpFreq[36][92] = 401;
        jpFreq[15][41] = 400;
        jpFreq[21][71] = 399;
        jpFreq[41][30] = 398;
        jpFreq[32][76] = 397;
        jpFreq[17][34] = 396;
        jpFreq[26][15] = 395;
        jpFreq[26][25] = 394;
        jpFreq[31][77] = 393;
        jpFreq[31][3] = 392;
        jpFreq[46][34] = 391;
        jpFreq[27][84] = 390;
        jpFreq[23][8] = 389;
        jpFreq[16][0] = 388;
        jpFreq[28][80] = 387;
        jpFreq[26][54] = 386;
        jpFreq[33][18] = 385;
        jpFreq[31][20] = 384;
        jpFreq[31][62] = 383;
        jpFreq[30][41] = 382;
        jpFreq[33][30] = 381;
        jpFreq[45][45] = 380;
        jpFreq[37][82] = 379;
        jpFreq[15][33] = 378;
        jpFreq[20][12] = 377;
        jpFreq[18][5] = 376;
        jpFreq[28][86] = 375;
        jpFreq[30][19] = 374;
        jpFreq[42][43] = 373;
        jpFreq[36][31] = 372;
        jpFreq[17][93] = 371;
        jpFreq[4][15] = 370;
        jpFreq[21][20] = 369;
        jpFreq[23][21] = 368;
        jpFreq[28][72] = 367;
        jpFreq[4][20] = 366;
        jpFreq[26][55] = 365;
        jpFreq[21][5] = 364;
        jpFreq[19][16] = 363;
        jpFreq[23][64] = 362;
        jpFreq[40][59] = 361;
        jpFreq[37][26] = 360;
        jpFreq[26][56] = 359;
        jpFreq[4][12] = 358;
        jpFreq[33][71] = 357;
        jpFreq[32][39] = 356;
        jpFreq[38][40] = 355;
        jpFreq[22][74] = 354;
        jpFreq[3][25] = 353;
        jpFreq[15][48] = 352;
        jpFreq[41][82] = 351;
        jpFreq[41][9] = 350;
        jpFreq[25][48] = 349;
        jpFreq[31][71] = 348;
        jpFreq[43][29] = 347;
        jpFreq[26][80] = 346;
        jpFreq[4][5] = 345;
        jpFreq[18][71] = 344;
        jpFreq[29][0] = 343;
        jpFreq[43][43] = 342;
        jpFreq[23][81] = 341;
        jpFreq[4][42] = 340;
        jpFreq[44][28] = 339;
        jpFreq[23][93] = 338;
        jpFreq[17][81] = 337;
        jpFreq[25][25] = 336;
        jpFreq[41][23] = 335;
        jpFreq[34][35] = 334;
        jpFreq[4][53] = 333;
        jpFreq[28][36] = 332;
        jpFreq[4][41] = 331;
        jpFreq[25][60] = 330;
        jpFreq[23][20] = 329;
        jpFreq[3][43] = 328;
        jpFreq[24][79] = 327;
        jpFreq[29][41] = 326;
        jpFreq[30][83] = 325;
        jpFreq[3][50] = 324;
        jpFreq[22][18] = 323;
        jpFreq[18][3] = 322;
        jpFreq[39][30] = 321;
        jpFreq[4][28] = 320;
        jpFreq[21][64] = 319;
        jpFreq[4][68] = 318;
        jpFreq[17][71] = 317;
        jpFreq[27][0] = 316;
        jpFreq[39][28] = 315;
        jpFreq[30][13] = 314;
        jpFreq[36][70] = 313;
        jpFreq[20][82] = 312;
        jpFreq[33][38] = 311;
        jpFreq[44][87] = 310;
        jpFreq[34][45] = 309;
        jpFreq[4][26] = 308;
        jpFreq[24][44] = 307;
        jpFreq[38][67] = 306;
        jpFreq[38][6] = 305;
        jpFreq[30][68] = 304;
        jpFreq[15][89] = 303;
        jpFreq[24][93] = 302;
        jpFreq[40][41] = 301;
        jpFreq[38][3] = 300;
        jpFreq[28][23] = 299;
        jpFreq[26][17] = 298;
        jpFreq[4][38] = 297;
        jpFreq[22][78] = 296;
        jpFreq[15][37] = 295;
        jpFreq[25][85] = 294;
        jpFreq[4][9] = 293;
        jpFreq[4][7] = 292;
        jpFreq[27][53] = 291;
        jpFreq[39][29] = 290;
        jpFreq[41][43] = 289;
        jpFreq[25][62] = 288;
        jpFreq[4][48] = 287;
        jpFreq[28][28] = 286;
        jpFreq[21][40] = 285;
        jpFreq[36][73] = 284;
        jpFreq[26][39] = 283;
        jpFreq[22][54] = 282;
        jpFreq[33][5] = 281;
        jpFreq[19][21] = 280;
        jpFreq[46][31] = 279;
        jpFreq[20][64] = 278;
        jpFreq[26][63] = 277;
        jpFreq[22][23] = 276;
        jpFreq[25][81] = 275;
        jpFreq[4][62] = 274;
        jpFreq[37][31] = 273;
        jpFreq[40][52] = 272;
        jpFreq[29][79] = 271;
        jpFreq[41][48] = 270;
        jpFreq[31][57] = 269;
        jpFreq[32][92] = 268;
        jpFreq[36][36] = 267;
        jpFreq[27][7] = 266;
        jpFreq[35][29] = 265;
        jpFreq[37][34] = 264;
        jpFreq[34][42] = 263;
        jpFreq[27][15] = 262;
        jpFreq[33][27] = 261;
        jpFreq[31][38] = 260;
        jpFreq[19][79] = 259;
        jpFreq[4][31] = 258;
        jpFreq[4][66] = 257;
        jpFreq[17][32] = 256;
        jpFreq[26][67] = 255;
        jpFreq[16][30] = 254;
        jpFreq[26][46] = 253;
        jpFreq[24][26] = 252;
        jpFreq[35][10] = 251;
        jpFreq[18][37] = 250;
        jpFreq[3][19] = 249;
        jpFreq[33][69] = 248;
        jpFreq[31][9] = 247;
        jpFreq[45][29] = 246;
        jpFreq[3][15] = 245;
        jpFreq[18][54] = 244;
        jpFreq[3][44] = 243;
        jpFreq[31][29] = 242;
        jpFreq[18][45] = 241;
        jpFreq[38][28] = 240;
        jpFreq[24][12] = 239;
        jpFreq[35][82] = 238;
        jpFreq[17][43] = 237;
        jpFreq[28][9] = 236;
        jpFreq[23][25] = 235;
        jpFreq[44][37] = 234;
        jpFreq[23][75] = 233;
        jpFreq[23][92] = 232;
        jpFreq[0][24] = 231;
        jpFreq[19][74] = 230;
        jpFreq[45][32] = 229;
        jpFreq[16][72] = 228;
        jpFreq[16][93] = 227;
        jpFreq[45][13] = 226;
        jpFreq[24][8] = 225;
        jpFreq[25][47] = 224;
        jpFreq[28][26] = 223;
        jpFreq[43][81] = 222;
        jpFreq[32][71] = 221;
        jpFreq[18][41] = 220;
        jpFreq[26][62] = 219;
        jpFreq[41][24] = 218;
        jpFreq[40][11] = 217;
        jpFreq[43][57] = 216;
        jpFreq[34][53] = 215;
        jpFreq[20][32] = 214;
        jpFreq[34][43] = 213;
        jpFreq[41][91] = 212;
        jpFreq[29][57] = 211;
        jpFreq[15][43] = 210;
        jpFreq[22][89] = 209;
        jpFreq[33][83] = 208;
        jpFreq[43][20] = 207;
        jpFreq[25][58] = 206;
        jpFreq[30][30] = 205;
        jpFreq[4][56] = 204;
        jpFreq[17][64] = 203;
        jpFreq[23][0] = 202;
        jpFreq[44][12] = 201;
        jpFreq[25][37] = 200;
        jpFreq[35][13] = 199;
        jpFreq[20][30] = 198;
        jpFreq[21][84] = 197;
        jpFreq[29][14] = 196;
        jpFreq[30][5] = 195;
        jpFreq[37][2] = 194;
        jpFreq[4][78] = 193;
        jpFreq[29][78] = 192;
        jpFreq[29][84] = 191;
        jpFreq[32][86] = 190;
        jpFreq[20][68] = 189;
        jpFreq[30][39] = 188;
        jpFreq[15][69] = 187;
        jpFreq[4][60] = 186;
        jpFreq[20][61] = 185;
        jpFreq[41][67] = 184;
        jpFreq[16][35] = 183;
        jpFreq[36][57] = 182;
        jpFreq[39][80] = 181;
        jpFreq[4][59] = 180;
        jpFreq[4][44] = 179;
        jpFreq[40][54] = 178;
        jpFreq[30][8] = 177;
        jpFreq[44][30] = 176;
        jpFreq[31][93] = 175;
        jpFreq[31][47] = 174;
        jpFreq[16][70] = 173;
        jpFreq[21][0] = 172;
        jpFreq[17][35] = 171;
        jpFreq[21][67] = 170;
        jpFreq[44][18] = 169;
        jpFreq[36][29] = 168;
        jpFreq[18][67] = 167;
        jpFreq[24][28] = 166;
        jpFreq[36][24] = 165;
        jpFreq[23][5] = 164;
        jpFreq[31][65] = 163;
        jpFreq[26][59] = 162;
        jpFreq[28][2] = 161;
        jpFreq[39][69] = 160;
        jpFreq[42][40] = 159;
        jpFreq[37][80] = 158;
        jpFreq[15][66] = 157;
        jpFreq[34][38] = 156;
        jpFreq[28][48] = 155;
        jpFreq[37][77] = 154;
        jpFreq[29][34] = 153;
        jpFreq[33][12] = 152;
        jpFreq[4][65] = 151;
        jpFreq[30][31] = 150;
        jpFreq[27][92] = 149;
        jpFreq[4][2] = 148;
        jpFreq[4][51] = 147;
        jpFreq[23][77] = 146;
        jpFreq[4][35] = 145;
        jpFreq[3][13] = 144;
        jpFreq[26][26] = 143;
        jpFreq[44][4] = 142;
        jpFreq[39][53] = 141;
        jpFreq[20][11] = 140;
        jpFreq[40][33] = 139;
        jpFreq[45][7] = 138;
        jpFreq[4][70] = 137;
        jpFreq[3][49] = 136;
        jpFreq[20][59] = 135;
        jpFreq[21][12] = 134;
        jpFreq[33][53] = 133;
        jpFreq[20][14] = 132;
        jpFreq[37][18] = 131;
        jpFreq[18][17] = 130;
        jpFreq[36][23] = 129;
        jpFreq[18][57] = 128;
        jpFreq[26][74] = 127;
        jpFreq[35][2] = 126;
        jpFreq[38][58] = 125;
        jpFreq[34][68] = 124;
        jpFreq[29][81] = 123;
        jpFreq[20][69] = 122;
        jpFreq[39][86] = 121;
        jpFreq[4][16] = 120;
        jpFreq[16][49] = 119;
        jpFreq[15][72] = 118;
        jpFreq[26][35] = 117;
        jpFreq[32][14] = 116;
        jpFreq[40][90] = 115;
        jpFreq[33][79] = 114;
        jpFreq[35][4] = 113;
        jpFreq[23][33] = 112;
        jpFreq[19][19] = 111;
        jpFreq[31][41] = 110;
        jpFreq[44][1] = 109;
        jpFreq[22][56] = 108;
        jpFreq[31][27] = 107;
        jpFreq[32][18] = 106;
        jpFreq[27][32] = 105;
        jpFreq[37][39] = 104;
        jpFreq[42][11] = 103;
        jpFreq[29][71] = 102;
        jpFreq[32][58] = 101;
        jpFreq[46][10] = 100;
        jpFreq[17][30] = 99;
        jpFreq[38][15] = 98;
        jpFreq[29][60] = 97;
        jpFreq[4][11] = 96;
        jpFreq[38][31] = 95;
        jpFreq[40][79] = 94;
        jpFreq[28][49] = 93;
        jpFreq[28][84] = 92;
        jpFreq[26][77] = 91;
        jpFreq[22][32] = 90;
        jpFreq[33][17] = 89;
        jpFreq[23][18] = 88;
        jpFreq[32][64] = 87;
        jpFreq[4][6] = 86;
        jpFreq[33][51] = 85;
        jpFreq[44][77] = 84;
        jpFreq[29][5] = 83;
        jpFreq[46][25] = 82;
        jpFreq[19][58] = 81;
        jpFreq[4][46] = 80;
        jpFreq[15][71] = 79;
        jpFreq[18][58] = 78;
        jpFreq[26][45] = 77;
        jpFreq[45][66] = 76;
        jpFreq[34][10] = 75;
        jpFreq[19][37] = 74;
        jpFreq[33][65] = 73;
        jpFreq[44][52] = 72;
        jpFreq[16][38] = 71;
        jpFreq[36][46] = 70;
        jpFreq[20][26] = 69;
        jpFreq[30][37] = 68;
        jpFreq[4][58] = 67;
        jpFreq[43][2] = 66;
        jpFreq[30][18] = 65;
        jpFreq[19][35] = 64;
        jpFreq[15][68] = 63;
        jpFreq[3][36] = 62;
        jpFreq[35][40] = 61;
        jpFreq[36][32] = 60;
        jpFreq[37][14] = 59;
        jpFreq[17][11] = 58;
        jpFreq[19][78] = 57;
        jpFreq[37][11] = 56;
        jpFreq[28][63] = 55;
        jpFreq[29][61] = 54;
        jpFreq[33][3] = 53;
        jpFreq[41][52] = 52;
        jpFreq[33][63] = 51;
        jpFreq[22][41] = 50;
        jpFreq[4][19] = 49;
        jpFreq[32][41] = 48;
        jpFreq[24][4] = 47;
        jpFreq[31][28] = 46;
        jpFreq[43][30] = 45;
        jpFreq[17][3] = 44;
        jpFreq[43][70] = 43;
        jpFreq[34][19] = 42;
        jpFreq[20][77] = 41;
        jpFreq[18][83] = 40;
        jpFreq[17][15] = 39;
        jpFreq[23][61] = 38;
        jpFreq[40][27] = 37;
        jpFreq[16][48] = 36;
        jpFreq[39][78] = 35;
        jpFreq[41][53] = 34;
        jpFreq[40][91] = 33;
        jpFreq[40][72] = 32;
        jpFreq[18][52] = 31;
        jpFreq[35][66] = 30;
        jpFreq[39][93] = 29;
        jpFreq[19][48] = 28;
        jpFreq[26][36] = 27;
        jpFreq[27][25] = 26;
        jpFreq[42][71] = 25;
        jpFreq[42][85] = 24;
        jpFreq[26][48] = 23;
        jpFreq[28][15] = 22;
        jpFreq[3][66] = 21;
        jpFreq[25][24] = 20;
        jpFreq[27][43] = 19;
        jpFreq[27][78] = 18;
        jpFreq[45][43] = 17;
        jpFreq[27][72] = 16;
        jpFreq[40][29] = 15;
        jpFreq[41][0] = 14;
        jpFreq[19][57] = 13;
        jpFreq[15][59] = 12;
        jpFreq[29][29] = 11;
        jpFreq[4][25] = 10;
        jpFreq[21][42] = 9;
        jpFreq[23][35] = 8;
        jpFreq[33][1] = 7;
        jpFreq[4][57] = 6;
        jpFreq[17][60] = 5;
        jpFreq[25][19] = 4;
        jpFreq[22][65] = 3;
        jpFreq[42][29] = 2;
        jpFreq[27][66] = 1;
        jpFreq[26][89] = 0;
    }

    /**
     * 初始化 字符集频率数组
     */
    void initialize_frequencies() {
        initializeFrequenciesArray();
        initialize_gbFreq();
        initialize_big5Freq();
        initialize_big5pFreq();
        initialize_eucTwFreq();
        initialize_gbkFreq();
        initialize_jpFreq();
        initialize_krFreq();
    }
}
