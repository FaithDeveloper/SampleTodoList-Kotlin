package com.kcs.sampletodolist.view.main

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.NavigationView
import android.support.v4.app.FragmentTransaction
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import com.kcs.sampletodolist.R
import com.kcs.sampletodolist.common.Constants
import com.kcs.sampletodolist.dto.TodoDTO
import com.kcs.sampletodolist.view.setting.SettingFragment
import com.kcs.sampletodolist.view.todo.AddTodoActivity
import com.kcs.sampletodolist.view.todo.TodoListFragment
import kotlinx.android.synthetic.main.activity_main_drawer.*
import kotlinx.android.synthetic.main.app_bar_main_drawer.*
import kotlinx.android.synthetic.main.app_bar_main_drawer.view.*
import kotlinx.android.synthetic.main.nav_header_main_drawer.*
import org.jetbrains.anko.startActivityForResult
import org.jetbrains.anko.support.v4.startActivityForResult

class MainDrawerActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private var currentPageConstant : PageConstant? = null
    private lateinit var todoListFragment: TodoListFragment
    private lateinit var settingFragment: SettingFragment
    private var userID : String? = null
    private var userEMail : String? = null

    /** =======================================
     *
     * override Function
     *
     * ========================================*/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_drawer)
        setSupportActionBar(toolbar)

        init()
    }

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, MainDrawerActivity::class.java)
        }
    }


    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else if (currentPageConstant == PageConstant.SETTING){
            replaceFragment(PageConstant.TODO, null)
        }else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main_drawer, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_help -> {
                Snackbar.make(fab, "문의사항은 http://faith-developer.tistory.com 에 문의해주세요.", Snackbar.LENGTH_LONG)
                        .setAction("Action", {
                            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("http://faith-developer.tistory.com")))
                        }).show()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.action_todo -> {
                // Handle the Todo_action
                replaceFragment(PageConstant.TODO, null)
            }
            R.id.action_setting -> {
                // Handle the Setting action
                replaceFragment(PageConstant.SETTING, null)
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(resultCode != Activity.RESULT_OK){
            return
        }

        when (currentPageConstant){
            PageConstant.TODO -> {
                todoListFragment.onActivityResult(requestCode, resultCode, data)
            }
            PageConstant.SETTING -> {
                settingFragment.onActivityResult(requestCode, resultCode, data)
            }
        }
    }

    /** =======================================
     *
     * Make Function
     *
     * ========================================*/
    // 화면 이동
    private fun replaceFragment(tag: PageConstant?, bundle: Bundle?) {
        if (currentPageConstant != null && currentPageConstant == tag) {
            return
        }

        var intentBundle = Bundle()

        if (bundle != null) {
            intentBundle = bundle
        }
        intentBundle.putString(Constants.INTENT_TAG, tag.toString())

        val ft: FragmentTransaction = supportFragmentManager.beginTransaction()

        when (tag){
            PageConstant .TODO -> {
                todoListFragment = TodoListFragment.newInstance(intentBundle)
                ft.replace(R.id.content_view, todoListFragment).setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit()
                supportActionBar?.title = "투두리스트"
                fab.visibility = View.VISIBLE
            }
            PageConstant.SETTING -> {
                settingFragment = SettingFragment.newInstance(intentBundle)
                ft.replace(R.id.content_view, settingFragment).setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit()
                supportActionBar?.title = "설정"
                fab.visibility = View.GONE
            }
        }

        currentPageConstant = tag
    }

    fun getUserID() : String? {
        return userID
    }

    fun init(){
        userID = intent.getStringExtra(Constants.INTENT_ID_DATA)
        userEMail = intent.getStringExtra(Constants.INTENT_EMAIL_DATA)
        val headerView = nav_view.getHeaderView(0)
        headerView.findViewById<TextView>(R.id.drawer_user_id).text = userID
        headerView.findViewById<TextView>(R.id.drawer_user_email).text = userEMail



        fab.setOnClickListener { view ->
           when (currentPageConstant){
               PageConstant.TODO -> {
                   startActivityForResult<AddTodoActivity>(Constants.ACTIVITY_REUSLT_ADD_TODO, Constants.INTENT_DATA to getUserID())
               }
               else -> {

               }
           }
        }

        // Navi Drawer 설정
        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
        replaceFragment(PageConstant.TODO, null)

        supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#3A99D9")))
    }
}
