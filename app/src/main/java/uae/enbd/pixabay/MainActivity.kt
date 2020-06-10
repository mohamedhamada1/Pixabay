package uae.enbd.pixabay

import android.content.Intent
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import uae.enbd.pixabay.databinding.ActivityMainBinding
import uae.enbd.pixabay.ui.base.BaseActivity
import javax.inject.Inject

class MainActivity : BaseActivity<ActivityMainBinding, ViewModel>(),
    HasSupportFragmentInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    @Inject
    lateinit var mViewModelFactory: ViewModelProvider.Factory

    override fun getViewModel(): ViewModel {
        return ViewModelProvider(this, mViewModelFactory).get(ViewModel::class.java)
    }


    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun supportFragmentInjector() = dispatchingAndroidInjector

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }
}
