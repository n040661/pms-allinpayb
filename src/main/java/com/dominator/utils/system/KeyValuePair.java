package com.dominator.utils.system;

/**
 * pair 工具类
 * 
 * @author gsh
 *
 * @param <K>
 * @param <V>
 */
public class KeyValuePair<K, V> {
	K key;
	V value;

	public KeyValuePair() {
	}

	public KeyValuePair(K _key, V _value) {
		this.key = _key;
		this.value = _value;
	}

	public K getKey() {
		return key;
	}

	public void setKey(K key) {
		this.key = key;
	}

	public V getValue() {
		return value;
	}

	public void setValue(V value) {
		this.value = value;
	}

}
