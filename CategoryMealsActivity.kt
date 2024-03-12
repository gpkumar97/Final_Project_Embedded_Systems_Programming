package com.example.food.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.example.food.adapter.CategoryMealListAdapter
import com.example.food.adapter.MostPopularItemAdapter
import com.example.food.databinding.ActivityCategoryMealsBinding
import com.example.food.ui.fragment.HomeFragmentWithViewModel
import com.example.food.viewmodel.CategoryMealsViewModel

class CategoryMealsActivity : AppCompatActivity() {

    private lateinit var binding:ActivityCategoryMealsBinding
    private lateinit var categoryName:String
    private lateinit var categoryMvvm:CategoryMealsViewModel
    private lateinit var mealsListAdapter: CategoryMealListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoryMealsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mealsListAdapter = CategoryMealListAdapter()

        prepareRecyclerView()
        categoryMvvm =ViewModelProviders.of(this)[CategoryMealsViewModel::class.java]
        getInformationFromIntent()


        categoryMvvm.getCategoryMeals(categoryName)
        observeMealsLiveData()


       onMealItemClick()

    }

    private fun onMealItemClick() {

        mealsListAdapter.onItemClick = {meal ->
            val intent = Intent(this,MealActivity::class.java)
            intent.putExtra(HomeFragmentWithViewModel.MEAL_ID,meal.idMeal)
            intent.putExtra(HomeFragmentWithViewModel.MEAL_NAME,meal.strMeal)
            intent.putExtra(HomeFragmentWithViewModel.MEAL_THUMB,meal.strMealThumb)
            startActivity(intent)
        }
    }

    private fun prepareRecyclerView() {
        mealsListAdapter=CategoryMealListAdapter()
        binding.rvCategoryItems.apply {
            layoutManager = GridLayoutManager(context,2,GridLayoutManager.VERTICAL,false)
            adapter=mealsListAdapter
        }
    }

    private fun observeMealsLiveData() {
        categoryMvvm.observeMealsLivedata().observe(this, Observer { mealsList ->
            binding.categoryName.text= "${categoryName} : ${mealsList.size}"
            mealsListAdapter.setMealsList(mealsList)
        })
    }

    private fun getInformationFromIntent() {
        val intent = intent
        categoryName = intent.getStringExtra(HomeFragmentWithViewModel.CATEGORY_NAME)!!
    }

}