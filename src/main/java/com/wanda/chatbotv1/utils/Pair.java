package com.wanda.chatbotv1.utils;


public class Pair<K,V> {
	public K key = null;
	public V value = null;
	public Pair(K key , V value){
		this.key = key;
		this.value = value;
	}
	public Pair(){};
	public K getFirst() {
		return key;
	}
	public void setFirst(K key) {
		this.key = key;
	}
	public V getSecond() {
		return value;
	}
	public void setSecond(V value) {
		this.value = value;
	}
	
	@Override
	public String toString(){
		return key.toString() + "=" + value.toString() ;
	}
	@Override
	public boolean equals(Object o) {
	       if (!(o instanceof Pair))
	           return false;
	       Pair w = (Pair) o;
	       if(w.key == null || key == null)
	    	   return false;
	       return w.key.equals(key);
	    }
	@Override
	public int hashCode() {
	       int result = 17;
	       result = 37 * result + key.hashCode();
	       return result;
	    }
	
}
