package com.yefan.testfunction.domain;

public class Persion {
	private String name;
	private Integer age;
	private Integer sex;
	private String 	work;
	private boolean isMarry;
	
	
	
	public Persion() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Persion(String name, Integer age, Integer sex, String work, boolean isMarry) {
		super();
		this.name = name;
		this.age = age;
		this.sex = sex;
		this.work = work;
		this.isMarry = isMarry;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getAge() {
		return age;
	}
	public void setAge(Integer age) {
		this.age = age;
	}
	public Integer getSex() {
		return sex;
	}
	public void setSex(Integer sex) {
		this.sex = sex;
	}
	public String getWork() {
		return work;
	}
	public void setWork(String work) {
		this.work = work;
	}
	public boolean isMarry() {
		return isMarry;
	}
	public void setMarry(boolean isMarry) {
		this.isMarry = isMarry;
	}
	@Override
	public String toString() {
		return "Persion [name=" + name + ", age=" + age + ", sex=" + sex + ", work=" + work + ", isMarry=" + isMarry
				+ "]";
	}
	
	
}
