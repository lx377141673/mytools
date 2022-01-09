package xyz.liangxin.utils.core.text.sensitiveword;


import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;


/**
 * 初始化敏感词库，将敏感词加入到HashMap中，构建DFA算法模型
 *
 * @author don't know
 */
public class SensitiveWordInit {

    /**
     * 截止键名
     */
    private static final String IS_END = "isEnd";

    /**
     * 敏感词字典
     */
    private Map<Object, Object> sensitiveWordMap;

    public SensitiveWordInit() {
        super();
    }

    public Map<Object, Object> getSensitiveWordMap() {
        return sensitiveWordMap;
    }

    /**
     * 读取敏感词库
     *
     * @return 敏感词库
     */
    public Map<Object, Object> initKeyWord() {
        return sensitiveWordMap;
    }

    /**
     * 添加敏感词信息
     *
     * @param sensitiveWord 敏感词信息, 数组
     */
    public void addSensitiveWord(String... sensitiveWord) {
        addSensitiveWordToHashMap(new HashSet<>(Arrays.asList(sensitiveWord)));
    }

    /**
     * 读取敏感词库，将敏感词放入HashSet中，构建一个DFA算法模型：<br>
     * <p>
     * 中 = {
     *      isEnd = 0
     *      国 = {<br>
     *           isEnd = 1
     *           人 = {isEnd = 0
     *                民 = {isEnd = 1}
     *                }
     *           男  = {
     *                  isEnd = 0
     *                   人 = {
     *                        isEnd = 1
     *                       }
     *               }
     *           }
     *      }
     *  五 = {
     *      isEnd = 0
     *      星 = {
     *          isEnd = 0
     *          红 = {
     *              isEnd = 0
     *              旗 = {
     *                   isEnd = 1
     *                  }
     *              }
     *          }
     *      }
     *</p>
     * @param keyWordSet 敏感词库
     */
    private void addSensitiveWordToHashMap(Set<String> keyWordSet) {
        //初始化敏感词容器，减少扩容操作
        sensitiveWordMap = Objects.nonNull(sensitiveWordMap) ? sensitiveWordMap : new HashMap<>(keyWordSet.size());
        //迭代keyWordSet
        keyWordSet.forEach(key -> {
            //关键字
            Map<Object, Object> nowMap = sensitiveWordMap;
            for (int i = 0; i < key.length(); i++) {
                //转换成char型
                char keyChar = key.charAt(i);
                //获取
                Object wordMap = nowMap.get(keyChar);
                //如果存在该key，直接赋值
                if (wordMap != null) {
                    nowMap = (Map<Object, Object>) wordMap;
                } else {
                    //不存在则，则构建一个map，同时将isEnd设置为0，因为他不是最后一个
                    Map<Object, Object> newWorMap = new HashMap<>(2);
                    //不是最后一个
                    newWorMap.put(SensitiveWordInit.IS_END, "0");
                    nowMap.put(keyChar, newWorMap);
                    nowMap = newWorMap;
                }
                if (i == key.length() - 1) {
                    //最后一个
                    nowMap.put(SensitiveWordInit.IS_END, "1");
                }
            }
        });
    }

}
