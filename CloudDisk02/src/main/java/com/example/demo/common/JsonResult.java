package com.example.demo.common;

public class JsonResult<T> {
	private T data;// ��ȷ����
	private String message;// ������Ϣ(��ȷ/����)
	private int state;// ״̬
	private static final int ERROR=0;// ����״̬
	private static final int SUCCESS=1;// ��ȷ״̬
	
	public JsonResult() {
		this.state=SUCCESS;
		this.message="OK";
	}
	/*
	 * ��json��ʽ������ȷ��Ϣ
	 */
	public JsonResult(T data){
		System.out.println("��ȷ��Ϣ");
		this.data = data;
		this.state=SUCCESS;
		this.message="OK";
	}	
	/*
	 * ��ȡ�쳣��Ϣ
	 */
	public JsonResult(Throwable e){
		this.state=ERROR;
		this.message=e.getMessage()	;
	}
	
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public T getData() {
		return data;
	}
	public void setData(T data) {
		this.data = data;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
}
