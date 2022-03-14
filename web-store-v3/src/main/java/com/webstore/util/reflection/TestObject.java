package com.webstore.util.reflection;

public class TestObject {
    String text;

    public TestObject(String text) {
        this.text = text;
    }

    public TestObject() {
    }

    public String getText() {

        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

  /*  public void printsAll(String text,String secondText){
        this.text = text+secondText;
        System.out.println("2");
        System.out.println(this.text);

    }
    public void printsAll(String text,String secondText,String textempty){
        this.text = text+secondText+textempty;
        System.out.println("3");
        System.out.println(this.text);

    }*/
    public void printsAll(String text,String second_text){
        this.text = text+second_text;
      // System.out.println("!run task:");
      // System.out.println(this.text);


    }

  /*  public void printsAll(int iiii,int trrrrrrrr){
        System.out.println("int");
      }*/
    /*public void printsAll(Integer iiii){
        System.out.println("integer");
    }*/

}
