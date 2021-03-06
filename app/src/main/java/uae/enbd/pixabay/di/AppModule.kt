/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uae.enbd.pixabay.di


import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import uae.enbd.pixabay.BuildConfig
import uae.enbd.pixabay.repository.api.PixabayService
import uae.enbd.pixabay.repository.local.HitDao
import uae.enbd.pixabay.repository.local.PixabayDb
import javax.inject.Singleton

@Module(includes = [ViewModelModule::class])
class AppModule {
    @Singleton
    @Provides
    fun providePixabayService(): PixabayService {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(LiveDataCallAdapterFactory())
            .build()
            .create(PixabayService::class.java)
    }

    @Singleton
    @Provides
    fun provideDb(app: Application): PixabayDb {
        return Room
            .databaseBuilder(app, PixabayDb::class.java, "pixabay.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideUserDao(db: PixabayDb): HitDao {
        return db.hitDao()
    }


}
