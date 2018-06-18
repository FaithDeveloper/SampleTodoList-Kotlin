# Sample TodoList 만들기

 `리스트`로 구성된 Sample TodoList 프로젝트는 `RecyclerView` 을 활용하여 만들었습니다.

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

## TodoList Sample Code 요구사항 

```
1. Realm 으로 회원 관리를 합니다.
2. Realm 으로 등록된 회원은 각자의 Todo 를 갖고 있습니다.
3. Todo를 작성화면에서 TodoList 화면으로 전환 시 추가된 데이터가 보입니다.
4. Todo 삭제는 실시간으로 Realm에 반영되어야 합니다.
5. Todo 한 것은 CheckBox로 표시할 수 있으며 체크는 남아있습니다.
6. Fragment을 통한 화면구성 합니다.
```

<br/>

## Kotlin Extention을 사용 시 Fragment 에서 주의 해야 할 사항

`Activity` 에서 Kotlin Extention을 사용할 때에는 `onCreate()` 에서 설정할 수 있었으나 비슷하게 `Fragment`에서 `onCreateView()` 에서 Kotlin Extention을 사용하면 `null` 을 리턴하여 Error을 발견할 수 있습니다. `Fragment` 에서 Kotlin Extention을 사용 시 `onViewCreated()` 에서 사용하여야 합니다.

> **Kotlin Extention을 Fragment에서 사용 시 주의 사항**
>
>  onViewCreated() 에서 Kotlin Extention을 사용해야 합니다.

```kotlin
 override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        txt_id.text = (activity as MainDrawerActivity).getUserID()
    }
```

<br/>

## 정리

`Realm`과 `Kotlin` 을 활용하여 `TodoList`을 구성하였습니다. `Fragment`와 `RecyclerView`을 연습할 수 있는 예제로 공부하는데 도움이 되길 바랍니다.
