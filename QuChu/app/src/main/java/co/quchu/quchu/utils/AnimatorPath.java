/*
 * Copyright (C) 2013 The Android Open Source Project
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
package co.quchu.quchu.utils;


import java.util.ArrayList;
import java.util.Collection;
public class AnimatorPath {
    
    // The points in the path
    ArrayList<PathPoint> mPoints = new ArrayList<>();

    public void moveTo(float x, float y) {
        mPoints.add(PathPoint.moveTo(x, y));
    }
    public void lineTo(float x, float y) {
        mPoints.add(PathPoint.lineTo(x, y));
    }
    public void curveTo(float c0X, float c0Y, float c1X, float c1Y, float x, float y) {
        mPoints.add(PathPoint.curveTo(c0X, c0Y, c1X, c1Y, x, y));
    }
    public Collection<PathPoint> getPoints() {
        return mPoints;
    }
}
