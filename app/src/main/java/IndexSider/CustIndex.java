package IndexSider;

import android.util.SparseIntArray;
import android.widget.SectionIndexer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by longyue on 2018/1/21.
 */

public class CustIndex implements SectionIndexer {
    private String[] mIndexString;
    private List<String> mDatas=new ArrayList<>();
    private SparseIntArray mAlphaMap;

    public CustIndex(String[] mIndexString, List<String> data) {
        this.mIndexString = mIndexString;
        mDatas.addAll(data);
        mAlphaMap = new SparseIntArray(this.mIndexString.length);
    }

    @Override
    public String[] getSections() {
        return mIndexString;
    }

    @Override
    public int getPositionForSection(int sectionIndex) {
        final SparseIntArray alphaMap = mAlphaMap;
        final List<String> items = mDatas;

        if (items == null || mIndexString == null) {
            return 0;
        }

        if (sectionIndex <= 0) {
            return 0;
        }
        if (sectionIndex >= mIndexString.length) {
            sectionIndex = mIndexString.length - 1;
        }

        int count = items.size();
        int start = 0;
        int end = count;
        int pos;

        char letter = mIndexString[sectionIndex].charAt(0);
        String targetLetter = Character.toString(letter);
        int key = letter;
        // 检查map是否已经保存了key为letter的对应索引,如果有的话直接返回,如果没有则进行查找
        if (Integer.MIN_VALUE != (pos = alphaMap.get(key, Integer.MIN_VALUE))) {
            if (pos < 0) {
                //如果没有,则将end设为一个极大值,以保证利用二分查找时,肯定能包含listview中所有item的索引
                pos = -pos;
                end = pos;
            } else {
                // Not approximate, this is the confirmed start of section, return it
                return pos;
            }
        }

        if (sectionIndex > 0) {
            int prevLetter = mIndexString[sectionIndex-1].charAt(0);
            int prevLetterPos = alphaMap.get(prevLetter, Integer.MIN_VALUE);
            if (prevLetterPos != Integer.MIN_VALUE) {
                start = Math.abs(prevLetterPos);
            }
        }

        pos = (end + start) / 2;

        while (pos < end) {
            // Get letter at pos
            String item = items.get(pos);
            String curName = "";
            if (item != null) {
                curName = item.charAt(0)+"";
            }
            if (curName == null) {
                if (pos == 0) {
                    break;
                } else {
                    pos--;
                    continue;
                }
            }
            int diff = compare(curName, targetLetter);
            if (diff != 0) {
                if (diff < 0) {
                    //如果已经到了Listview的末尾,但仍没有索引字符对应的item,就将最末Item的索引返回.
                    start = pos + 1;
                    if (start >= count) {
                        pos = count;
                        break;
                    }
                } else {
                    end = pos;
                }
            } else {
                // They're the same, but that doesn't mean it's the start
                if (start == pos) {
                    // This is it
                    break;
                } else {
                    // Need to go further lower to find the starting row
                    end = pos;
                }
            }
            pos = (start + end) / 2;
        }
        alphaMap.put(key, pos);
        return pos;
    }

    @Override
    public int getSectionForPosition(int position) {
        return 0;
    }


    protected int compare(String firstLetter, String secondLetter) {
        if (firstLetter == null||firstLetter.length() == 0) {
            firstLetter = " ";
        } else {
            firstLetter = firstLetter.substring(0, 1);
        }
        if (secondLetter == null||secondLetter.length() == 0) {
            secondLetter = " ";
        } else {
            secondLetter = secondLetter.substring(0, 1);
        }

        return firstLetter.compareTo(secondLetter);
    }
}
