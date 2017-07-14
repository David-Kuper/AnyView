package com.davidkuper.anyview;

/**
 * Created by malingyi on 2017/3/28.
 */

/**
 * View和Bean的绑定关系，一个view可以绑定多个bean,一个bean可以绑定多个view
 */
public class AnyViewBind{
  public int preNum;//预加载数量
  public Class viewClazz; //CardView.class类别
  public Class beanClazz;  //对应Bean.class类别
  public int type;

  /**
   * 1、引用相同为相等
   * 2、绑定关系相同为相等
   * 3、其他都为不等
   * @param obj
   * @return
   */
  @Override public boolean equals(Object obj) {
    if (this == obj){
      return true;
    }
    if (obj instanceof AnyViewBind){
      //view 和 bean相等，即已存在绑定关系
      if (this.viewClazz == ((AnyViewBind) obj).viewClazz && this.beanClazz == ((AnyViewBind) obj).beanClazz){
        return true;
      }else {
        return false;
      }
    }else {
      return false;
    }
  }

  /**
   * 为了使得AnyViewBind能够作为key，用在HashMap.containKey()中，选择所有对象的hashCode都相同，只比较equals()
   * 返回统一的hashCode
   * @return
   */
  @Override public int hashCode() {
    return 0Xfffff;
  }

  @Override public String toString() {
    return "viewClass = " + viewClazz
        +"    beanClass = " + beanClazz + "   preNum = " + preNum + "   type = " + type;
  }
}
