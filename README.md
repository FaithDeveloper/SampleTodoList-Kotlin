# Sample TodoList 만들기

 `리스트`로 구성된 Sample TodoList 프로젝트는 `RecyclerView` 을 활용하여 만들었습니다.

<br/>

## TodoList Sample Code 요구사항

```
1. Realm 으로 회원 관리를 합니다.
2. Realm 으로 등록된 회원은 각자의 Todo 를 갖고 있습니다.
3. Todo를 작성화면에서 TodoList 화면으로 전환 시 추가된 데이터가 보입니다.
4. Todo 삭제는 실시간으로 Realm에 반영되어야 합니다.
5. Todo 한 것은 CheckBox로 표시할 수 있으며 체크는 남아있습니다.
6. Fragment을 통한 화면구성 합니다.
7. Drawer을 활용하여 글로벌메뉴를 구성합니다.
8. 메뉴의 '도움말' 기능이 추가하여 사이트로 이동하도록 합니다.
```

<br/>

## 실행화면

<img src="https://github.com/FaithDeveloper/TIL/blob/master/res/todolist_sample.gif" data-canonical-src="https://github.com/FaithDeveloper/TIL/blob/master/res/todolist_sample.gif" width="300" height="533" />

<br/>

## RecyclerView

RecyclerView는 ViewGroup의 서브 클래스로, 자식 View 객체들의 리스트를 보여줍니다. 이때 리스트의 각 항목이 하나의 자식 View 객체가 됩니다. 리스트에 있는 모든 항목에 대해 하나씩 생성한다면 100개, 1000개의 항목을 갖은 리스트인 경우 정상적으로 실행 될 수 없습니다. RecyclerView는 한 화면에 모든 자식 View을 생성하는 것이 아니라 한 화면을 채우는데 충분한 12개만 생성합니다. (화면 구성에 따라 달라질 수 있습니다.) 그리고 화면이 스크롤되면서 View가 화면을 벗어날 때 RecyclerView는 그 View을 버리지 않고 재활용합니다. 이름 그대로 RecyclerView는 끊임없이 View을 재활용 합니다.

<br/>

## ViewHolder와 Adapter

ViewHolder와 Adapter는 List를 구성하는데 꼭 필요한 개념입니다. RecyclerView에서 Adapter와 ViewHolder을 구성하면 이렇게 됩니다. 필수로 입력해야 할 메서드는 `onCreateViewHolder()` , `getItemCount()` , `onBindViewHolder()` 로 구성됩니다.

```kotlin
class TodoAdapter(val context: Context, val dataList: List<TodoDTO>?, val listener: OnItemClickListener) : RecyclerView.Adapter<TodoAdapter.ViewHolder>(){
    private val inflater: LayoutInflater = LayoutInflater.from(context)

    private var datas : MutableList<TodoDTO>? = null

    /**
     * View Holder 연결
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = inflater.inflate(R.layout.cell_todo_list, parent, false)
        return ViewHolder(view)
    }

    /**
     * Recycler 에 표시할 셀 갯수
     */
    override fun getItemCount(): Int {
        if(datas == null){
            return 0
        }
        return datas!!.size
    }

    /**
     * View가 Bind 되었을 때 설정 
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(holder == null || datas == null){
            return
        }

     	//...
    }

    /**
     * 데이터 업데이트
     */
    fun setDataList(dataList: List<TodoDTO>?){
        if(dataList == null){
            return
        }
        this@TodoAdapter.datas = dataList.toMutableList()
    }

    /**
     * ViewHolder
     * */
    class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView){
        val layout_container= itemView?.findViewById<LinearLayout>(R.id.layout_container)
        val check_todo = itemView?.findViewById<CheckBox>(R.id.check_todo)
        val txt_todo = itemView?.findViewById<TextView>(R.id.txt_todo)
        val btn_delete = itemView?.findViewById<Button>(R.id.btn_delete)
    }
}
```

실제 RecyclerView에 연결하는 코드는 다음과 같습니다. 여기서 중요한 것은 RecyclerView의 layoutManager을 설정해야 합니다.  layoutManager는 어떤 형태의 Recycler을 할지 설정하게 됩니다.

```kotlin
private fun initTodoAdapter(){
        // 중요!! RecyclerView을 사용 시 어떤 형태의 Recycler을 할지 설정해야한다.
        list_todo.layoutManager = LinearLayoutManager(activity)
        adapter = TodoAdapter(activity!!.applicationContext, userTodo, object : OnItemClickListener{
        // ...
        list_todo.adapter = adapter
        // ...
    }
```

<br/>

## Kotlin Extention을 사용 시 Fragment 에서 주의 해야 할 사항

`Activity` 에서 [Kotlin Extention](https://github.com/FaithDeveloper/TIL/blob/master/Android/Kotlin%20Android%20Extensions.md)을 사용할 때에는 `onCreate()` 에서 설정할 수 있었으나 비슷하게 `Fragment`에서 `onCreateView()` 에서 Kotlin Extention을 사용하면 `null` 을 리턴하여 Error을 발견할 수 있습니다. `Fragment` 에서 Kotlin Extention을 사용 시 `onViewCreated()` 에서 사용하여야 합니다.

> **Kotlin Extention을 Fragment에서 사용 시 주의 사항**
>
>  onViewCreated() 에서 Kotlin Extention을 사용해야 합니다.

```kotlin
 override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        txt_id.text = (activity as MainDrawerActivity).getUserID()
    }
```

<br/>

## ActionBarDrawerToggle 

Android에서 제공하는 DrawerToggle 을 활용한다면 보다 쉽게 메뉴를 구성할 수 있니다.  Activity  생성 시 `Nabvigation Drawer Activity` 로 생성 하면 간편히 메뉴를 만들 수 있습니다. 

Navi Drawer 설정 시 Acitivity에 `ActionBarDrawerToggle` 등록 하며 메뉴 선택은 `setNavigationItemSelectedListener` 로 구성하여 동작합니다.

**MainDrawerActivity.java** 

```kotlin 
class MainDrawerActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
     override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_drawer)
        setSupportActionBar(toolbar)

        init()
    }
    
    // Navi Drawer 설정
    fun init(){
        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
            // Handle navigation view item clicks here.
            when (item.itemId) {
                R.id.action_todo -> {
                    // Handle the Todo action
                }
                R.id.action_setting -> {
                   // Handle the Setting action
                }
            }

            drawer_layout.closeDrawer(GravityCompat.START)
            return true
    }
}
```

**activity_main_drawer.xml**

```xml
<?xml version="1.0" encoding="utf-8"?>
<android.support.v4.widget.DrawerLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:id="@+id/drawer_layout"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:fitsSystemWindows="true"
    tools:openDrawer="start">

    <include
        layout="@layout/app_bar_main_drawer"
        android:layout_width="match_parent"
        android:layout_height="match_parent" />

    <android.support.design.widget.NavigationView
        android:id="@+id/nav_view"
        android:layout_width="wrap_content"
        android:layout_height="match_parent"
        android:layout_gravity="start"
        android:fitsSystemWindows="true"
        app:headerLayout="@layout/nav_header_main_drawer"
        app:menu="@menu/drawer_menu" />

</android.support.v4.widget.DrawerLayout>
```

### **DrawerLayout 구성 시 주의 사항**

DrawerLayout을 잘못 구성한 경우 ActionBar와 구성한 화면이 겹치는 경우가 있습니다.  DrawerLayout 안에 포함될 xml 에 `context`, `showIn`, `layout_behavior` 설정으로 해당 이슈를 해결할 수 있습니다. 

```xml
xmlns:app="http://schemas.android.com/apk/res-auto"
xmlns:tools="http://schemas.android.com/tools"
tools:context=".view.main.MainDrawerActivity"
tools:showIn="@layout/app_bar_main_drawer"
app:layout_behavior="@string/appbar_scrolling_view_behavior"
```

**content_main.xml**

 ```kotlin
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    tools:context=".view.main.MainDrawerActivity"
    tools:showIn="@layout/app_bar_main_drawer"
    app:layout_behavior="@string/appbar_scrolling_view_behavior"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="@color/black"
    android:orientation="vertical">
    <FrameLayout
        android:id="@+id/content_view"
        android:background="@color/white"
        android:layout_width="match_parent"
        android:layout_height="match_parent" />
</LinearLayout>
 ```

<br/>

## ActionBar 'Back' Button 구성 방법

ActionBar에서  'Back' 버튼을 지원하는거 알고 계셨나요? 간단한 설정으로 쉽게 구성할 수 있습니다. 

 'Back' 버튼 구성 시 `ParentActivity`와 `ChildActivity` 를 구분하여 설정해야 합니다.

`ParentActivity`와 `ChlidActivity` 선언은 `manifests` 에서 합니다. 

**manifests.xml**

```xml
<activity
          android:name=".view.join.JoinActivity"
          android:launchMode="singleTop"
          android:parentActivityName=".view.login.LoginActivity"
          android:screenOrientation="portrait">
    <meta-data
               android:name="android.support.PARENT_ACTIVITY"
               android:value=".view.login.LoginActivity" />
</activity>
```

'Back' Button을 표시할 Activity 에서도 버튼 표시 선언을 해야합니다.  `ChildActivity` 에 표시 된 `setDisplayHomeAsUpEnabled(true)`  선언하여 'Back' Button을 표시합니다.

```kotlin
supportActionBar?.setDisplayHomeAsUpEnabled(true)
```

`setDisplayHomeAsUpEnabled` 의 `true` 로 했을 경우 ActionBar에 표시만 되고 그것에 대한 액션은 이뤄지지 않습니다. `NavUtils.navigateUpFromSameTask(this)` 을 통하여 이전 화면으로 뒤돌아가는 로직을 구현합니다.

```kotlin
override fun onOptionsItemSelected(item: MenuItem?): Boolean {
    when (item?.itemId){
        android.R.id.home -> {
            NavUtils.navigateUpFromSameTask(this)
            return true
        }
        else -> {
            return super.onOptionsItemSelected(item)
        }
    }
}
```

> **'Back Button' 구현 시 주의사항**
>
> `Back Button` 클릭 시 `onOptionsItemSelected()` 을 호출하게 됩니다. 여기서 `item?itemID` 로 `android.R.id.home` 을 비교해야지 `item?groupID` 로 비교하면 안됩니다. 알게모르게 실수 할 수 있는 부분이라서 주의해주세요.

<br/>

## 정리

`Realm`과 `Kotlin` 을 활용하여 `TodoList`을 구성하였습니다. `Fragment`와 `RecyclerView`을 연습할 수 있는 예제로 공부하는데 도움이 되길 바랍니다.

Kotlin Extention 개념은 [Android Kotlin Extention](https://github.com/FaithDeveloper/TIL/blob/master/Android/Kotlin%20Android%20Extensions.md) 에서 확인 가능 합니다.