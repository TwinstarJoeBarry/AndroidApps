package edu.ncc.nest.nestapp.CheckExpirationDate.Activities;

/**
 *
 * Copyright (C) 2020 The LibreFoodPantry Developers.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import edu.ncc.nest.nestapp.R;
import edu.ncc.nest.nestapp.nestdb.NestDBActivity;
import edu.ncc.nest.nestapp.nestdb.NestDBDataSource;

/*
 * CheckExpirationDateActivity: This is the underlying activity for the fragments of the
 * CheckExpirationDate feature.
 */
public class CheckExpirationDateActivity extends NestDBActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /* Don't set content view here so that fragments are NOT created while the database is
         * loading */


    }

    @Override
    protected void onLoadSuccess(@NonNull NestDBDataSource nestDBDataSource) {
        super.onLoadSuccess(nestDBDataSource);

        /* Set the content view and support action toolbar here, so that fragments are NOT created
         * until the database successfully loads. */
        setContentView(R.layout.activity_check_expiration_date);

        setSupportActionBar(findViewById(R.id.check_expiration_date_toolbar));

    }

    @Override
    protected void onLoadError(@NonNull Throwable throwable) {
        super.onLoadError(throwable);

    }

}