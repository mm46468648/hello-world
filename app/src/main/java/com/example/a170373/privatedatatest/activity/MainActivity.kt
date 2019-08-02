package com.example.a170373.privatedatatest.activity

import android.arch.lifecycle.LifecycleObserver
import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.a170373.privatedatatest.R
import com.example.a170373.privatedatatest.dao.MDbDao
import com.example.a170373.privatedatatest.recycle_adapter.FileListAdapter
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.io.InputStream
import java.io.OutputStream

class MainActivity : AppCompatActivity() {
    val TAG = MainActivity::class.java.simpleName
    val fileList:ArrayList<String> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        startActivity(Intent(this,SecondActivity::class.java))

        val absoluteFilePath = filesDir.absolutePath
        fileList.add("读取私有file目录-$absoluteFilePath")
        fileList.add("写入私有file目录-$absoluteFilePath")
        val absoluteCachePath = cacheDir.absolutePath
        fileList.add("读取私有cache目录-$absoluteCachePath")
        fileList.add("写入私有cache目录-$absoluteCachePath")
        val absoluteCustomPath = getDir("custom", Context.MODE_PRIVATE).absolutePath
        fileList.add("读取私有custom目录-$absoluteCustomPath")
        fileList.add("写入私有custom目录-$absoluteCustomPath")


        val externalStoragePath = Environment.getExternalStorageDirectory().path
        fileList.add("读取私有externalStoragePath目录-$externalStoragePath")
        fileList.add("写入私有externalStoragePath目录-$externalStoragePath")

        val externalCachePath = externalCacheDir.path
        fileList.add("读取私有externalCachePath目录-$externalCachePath")
        fileList.add("写入私有externalCachePath目录-$externalCachePath")
//        var openFileOutput = openFileOutput("data", Context.MODE_PRIVATE)
        recyclerView.layoutManager = GridLayoutManager(this,2)
        var fileListAdapter = FileListAdapter(this, fileList)
        recyclerView.adapter = fileListAdapter
        fileListAdapter.onItemClickLis = {
            //1.向文件夹中写入
            //2.从文件夹中读
            if(it.startsWith("读取")){
                readFile(it.split("-")[1]+"/test.txt")
            }else if(it.startsWith("写入")){
                writeFile(it.split("-")[1]+"/test.txt")
            }
        }

    }

    fun readFile(filepath:String){
        var readText = File(filepath).readText()
        Log.e(TAG,readText)
    }

    fun writeFile(filepath: String){
        var file = File(filepath)
        file.writeText("测试写入$filepath")
    }

    fun readDB(view:View){
        val find = MDbDao.find()
        Log.e(TAG,"${find.size}")
        find.forEach {
            Log.e(TAG,it)
        }
    }

    fun writeDB(view: View){
        MDbDao.insert()
    }
}
