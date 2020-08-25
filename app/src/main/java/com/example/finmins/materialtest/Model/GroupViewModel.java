package com.example.finmins.materialtest.Model;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.finmins.materialtest.Group;
import com.example.finmins.materialtest.HttpClientUtils;
import com.example.finmins.materialtest.MemberInGroup;

import java.lang.reflect.Member;
import java.util.Calendar;
import java.util.List;

public class GroupViewModel extends ViewModel {
  private MutableLiveData<List<Group>> groupList ;   //群列表
  private MutableLiveData<List<MemberInGroup>> memberList; //群成员列表
  private MutableLiveData<Integer> searchgroupID  ;  //搜索群的群号
  private  MutableLiveData<String> searchName ; //搜索群的名字
  private MutableLiveData<Integer> searchImg ; //搜索群的头像
  private MutableLiveData<Integer> addGroupId ; //创建群的群号
  private MutableLiveData<String> addGroupName ; //创建群的名字
  private MutableLiveData<Integer> isVip ;  //是否是群管理员
   private String userEmail ; //用户邮箱
    private HttpClientUtils httpClientUtils  = new HttpClientUtils() ;
//获取用户邮箱
  public String getUserEmail(){
      return userEmail;
  }

//  设置用户邮箱
    public void setUserEmail(String email){
      userEmail = email;
    }
    //设置搜索的群头像
    public void setSearchImg(int i ){
      this.searchImg.setValue(i);
    }

    //设置搜索群的群名
    public void setSearchName(String s){
      this.searchName.setValue(s);

    }
    //获取搜索的群名
  public MutableLiveData<String> getSearchName (){
      if (   searchName ==null){
          searchName = new MutableLiveData<String>();
          searchName .setValue(null);
      }
      return   searchName;
  }

    //获取 创建群的名
    public MutableLiveData<String> getAddGroupName (){
        if (  addGroupName  ==null){
          addGroupName = new MutableLiveData<String>();
           addGroupName.setValue(null);
        }
        return   addGroupName;
    }
    //获取搜索的群号
    public MutableLiveData<Integer> getSearchgroupID (){
        if ( searchgroupID   ==null){
       searchgroupID    = new MutableLiveData<Integer>();
          searchgroupID .setValue(null);
        }
        return   searchgroupID;
    }
    //获取搜索群的头像
    public MutableLiveData<Integer> getSearchImg (){
        if (  searchImg  ==null){
         searchImg  = new MutableLiveData<Integer>();
         searchImg  .setValue(null);
        }
        return   searchImg;
    }

    //获取创建群的群号
    public MutableLiveData<Integer> getAddGroupId (){
        if (  addGroupId  ==null){
         addGroupId  = new MutableLiveData<Integer>();
         addGroupId  .setValue(null);
        }
        return  addGroupId ;
    }

    //获取是否是管理员
    public MutableLiveData<Integer> getIsVip(){
        if (  isVip  ==null){
          isVip = new MutableLiveData<Integer>();
           isVip.setValue(null);
        }
        return  isVip ;
    }

    //获取群列表
    public MutableLiveData<List<Group>> getGroupList (){
        if (  groupList  ==null){
          groupList = new MutableLiveData<List<Group>>();
         groupList  .setValue(null);
        }
        return groupList  ;
    }
    //设置群列表
    public void  setGroupList(List<Group> groupList){
      this.groupList.setValue(groupList);
    }


    //获取群成员列表
    public MutableLiveData<List<MemberInGroup>> getMemberList (){
        if (  memberList ==null){
          memberList = new MutableLiveData<List<MemberInGroup>>();
           memberList.setValue(null);
        }
        return memberList  ;
    }

    //添加群
    public int requestAddGroup(String selfEmail,String groupNmae){
      String response ;
      response = httpClientUtils.send("","","");


      return 1;
    }

    //创建群
    public int requestCreateGroup(String selfEmail,String groupName){
      String response;
      response = httpClientUtils.send("","","");

      return 1;
    }
        //删除一个群的成员
    public int requestDeleteMember(String groupnName,String memberEmail){
      String response;
      response = httpClientUtils.send("","","");

      return 1;
    }
    //删除一个群
    public int requestDeleteGroup(String groupName){
      String response;
      response = httpClientUtils.send("","","");
      return 1;
    }

    //从数据库获取--群列表


    //从数据库获取--群成员列表
     public void requestMemberLsit(String groupName){
      String response ;
      response = httpClientUtils.send("","","");

      memberList.setValue( null);
     }

  //从数据库获取群列表
    public int requestGroupList(String userEmail){
      String response ;
      response = httpClientUtils.send("","","");


//     把群list赋值进去   setGroupList(  );
      return 1;
    }


     //从数据库获取--搜索群的头像
        public  int requestGroupImg(String grouName ){
            String response ;
            response = httpClientUtils.send("","","");

            setSearchImg(1);
            setSearchName(grouName);
            return 1;
        }




    //发送打卡用户给后台
    //传个群名，用户名，打卡年，打卡月，打卡天
    public int requestFinishAmember(String groupName,String memberEmail,String date){
      String response ;
      response= httpClientUtils.send("","","");
      return 1;
    }
}
