package com.hots.common.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.hots.common.exception.BasicException;

public class CollectionUtil {

    /**
     * List 基础类型 排序
     *
     * @param sourceList 源数据 排序集合
     * @param sortType   升序 还是 降序，默认升序
     * @param            <T> 泛型T
     * @return List
     */
    public <T> void sortList(List<T> sourceList, boolean sortType) {

        Collections.sort(sourceList, new Comparator<Object>() {

            @Override
            public int compare(Object o1, Object o2) {
                return getRetCommon(o1, o2, sortType);
            }

        });
    }

    /**
     * Set 基础类型 排序
     *
     * @param sourceSet 源数据 排序集合
     * @param sortType  升序 还是 降序，默认升序
     * @return 排序后，以List形式返回
     */
    public <T> List<T> sortSet(Set<T> sourceSet, boolean sortType) {

        List<T> result = new ArrayList<>(sourceSet);

        Collections.sort(result, new Comparator<Object>() {

            @Override
            public int compare(Object o1, Object o2) {
                return getRetCommon(o1, o2, sortType);
            }

        });

        return result;
    }

    /**
     * List 对象根据指定字段排序（单个字段）
     *
     * @param list      源数据 排序集合
     * @param fieldName 排序的数据字段名称
     * @param sortType  升序 还是 降序，默认升序
     */
    public <T> void sortList(List<T> list, String fieldName, boolean sortType) {
        if (list != null && list.size() > 0) {
            Collections.sort(list, new Comparator<T>() {
                @Override
                public int compare(T t1, T t2) {
                    return getRetByField(t1, t2, fieldName, sortType);
                }
            });
        }
    }

    /**
     * List 对象根据指定字段排序(多字段，先后排序)
     *
     * @param list 源数据 排序集合
     * @param      sortMap:指定字段排序( LinkedHashMap<排序的数据字段名称, boolean>)
     * 
     */
    public <T> void sortList(List<T> list, final Map<String, Boolean> sortMap) {

        // 指定字段排序
        if (sortMap != null && sortMap.size() > 0) {
            Collections.sort(list, new Comparator<T>() {
                @Override
                public int compare(T t1, T t2) {
                    int ref = 0;
                    for (String fieldName : sortMap.keySet()) {
                        ref = getRetByField(t1, t2, fieldName,
                                sortMap.get(fieldName) == null ? true : sortMap.get(fieldName));
                        if (ref != 0) {
                            return ref;
                        }
                    }
                    return ref;
                }
            });
        }
    }

    /**
     * Map按照KEY值排序(KEY类型是通用类型)
     *
     * @param sourceMap 源数据 排序集合
     * @param sortType  升序 还是 降序
     */
    public <K, V> List<Map.Entry<K, V>> sortMapByKey(Map<K, V> sourceMap, boolean sortType) {

        List<Map.Entry<K, V>> result = null;

        if (sourceMap != null && sourceMap.size() > 0) {

            result = new ArrayList<>(sourceMap.entrySet());

            Collections.sort(result, new Comparator<Map.Entry<K, V>>() {
                @Override
                public int compare(Map.Entry<K, V> e1, Map.Entry<K, V> e2) {
                    Object o1 = e1.getKey();
                    Object o2 = e2.getKey();

                    return getRetCommon(o1, o2, sortType);
                }

            });
        }

        return result;
    }

    /**
     * Map按照VALUE值排序(VALUE类型是通用类型)
     *
     * @param sourceMap 源数据 排序集合
     * @param sortType  升序 还是 降序
     */
    public <K, V> List<Map.Entry<K, V>> sortMapByValue(Map<K, V> sourceMap, boolean sortType) {

        List<Map.Entry<K, V>> result = null;

        if (sourceMap != null && sourceMap.size() > 0) {

            result = new ArrayList<>(sourceMap.entrySet());

            Collections.sort(result, new Comparator<Map.Entry<K, V>>() {
                @Override
                public int compare(Map.Entry<K, V> e1, Map.Entry<K, V> e2) {
                    Object o1 = e1.getValue();
                    Object o2 = e2.getValue();

                    return getRetCommon(o1, o2, sortType);
                }

            });
        }

        return result;
    }

    /**
     * Map按照VALUE指定字段排序
     *
     * @param sourceMap 源数据 排序集合
     * @param fieldName 排序字段名称
     * @param sortType  升序 还是 降序
     */
    public <K, V> List<Map.Entry<K, V>> sortMapByValueField(Map<K, V> sourceMap, String fieldName, boolean sortType) {
        List<Map.Entry<K, V>> result = null;

        if (sourceMap != null && sourceMap.size() > 0) {

            result = new ArrayList<>(sourceMap.entrySet());

            Collections.sort(result, new Comparator<Map.Entry<K, V>>() {
                @Override
                public int compare(Map.Entry<K, V> e1, Map.Entry<K, V> e2) {
                    V o1 = e1.getValue();
                    V o2 = e2.getValue();

                    return getRetByField(o1, o2, fieldName, sortType);
                }

            });

        }
        return result;
    }

    public <T> String arrToStr(T[] target, String split) {

        StringBuilder builder = new StringBuilder();
        if (target != null && target.length > 0) {
            for (T tmp : target) {
                String value = String.valueOf(tmp);
                if (!StringUtil.isEmpty(value)) {
                    builder.append(split).append(value);
                }
            }

            return builder.substring(split.length(), builder.length());
        }

        return builder.toString();
    }

    /**
     * 泛型，指定字段，反射取通用类型值后，比较
     * 
     * @param t1
     * @param t2
     * @param fieldName
     * @param sortType
     * @return
     */
    private <T> int getRetByField(T t1, T t2, String fieldName, boolean sortType) {

        Object o1 = getFieldValue(t1, fieldName);
        Object o2 = getFieldValue(t2, fieldName);

        return getRetCommon(o1, o2, sortType);
    }

    /**
     * 通用类型排序
     * 
     * @param o1
     * @param o2
     * @param    sortType: true:升序；false：降序
     * @return
     */
    private int getRetCommon(Object o1, Object o2, boolean sortType) {
        int ret = 0;
        if (o1 == null && o2 == null) {
            ret = 0;
        } else if (o1 == null) {
            ret = -1;
        } else if (o2 == null) {
            ret = 1;
        } else if (o1 instanceof Integer) {
            ret = ((Integer) o1).compareTo((Integer) o2);
        } else if (o1 instanceof Long) {
            ret = ((Long) o1).compareTo((Long) o2);
        } else if (o1 instanceof Float) {
            ret = ((Float) o1).compareTo((Float) o2);
        } else if (o1 instanceof Date) {
            ret = ((Date) o1).compareTo((Date) o2);
        } else if (o1 instanceof Double) {
            ret = ((Double) o1).compareTo((Double) o2);
        } else if (o1 instanceof String) {
            ret = ((String) o1).compareTo((String) o2);
        } else {
            ret = String.valueOf(o1).compareTo(String.valueOf(o2));
        }

        if (!sortType) {
            ret *= -1;
        }

        return ret;
    }

    /**
     * 根据字段名，获取字段值
     */
    private <T> Object getFieldValue(T t, String fieldName) {

        try {

            if (t != null && !StringUtil.isEmpty(fieldName)) {
                Field field = t.getClass().getDeclaredField(fieldName);
                field.setAccessible(true);
                Class<?> type = field.getType();
                if (field.get(t) != null) {
                    if (type == int.class) {
                        return field.getInt(t);
                    } else if (type == double.class) {
                        return field.getDouble(t);
                    } else if (type == long.class) {
                        return field.getLong(t);
                    } else if (type == float.class) {
                        return field.getFloat(t);
                    } else {
                        return field.get(t);
                    }
                } else {
                    return null;
                }
            }

        } catch (Exception e) {
            new BasicException("通过反射获取字段值失败", "类型:" + t.getClass() + ",字段：" + fieldName, e);
        }

        return null;
    }
}
