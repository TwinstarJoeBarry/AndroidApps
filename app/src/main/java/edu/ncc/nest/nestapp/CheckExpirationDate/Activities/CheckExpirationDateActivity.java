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

import edu.ncc.nest.nestapp.NestDBDataSource;
import edu.ncc.nest.nestapp.R;

/*
 * CheckExpirationDateActivity: This is the underlying activity for the fragments of the
 * CheckExpirationDate feature.
 */
public class CheckExpirationDateActivity extends NestDBDataSource.NestDBActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_check_expiration_date);

        setSupportActionBar(findViewById(R.id.toolbar));

    }

}