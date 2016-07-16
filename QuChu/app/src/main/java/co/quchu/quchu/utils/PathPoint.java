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

public class PathPoint {
    public static final int MOVE = 0;
    public static final int LINE = 1;
    public static final int CURVE = 2;

    public float mX;
    public float mY;
    float mControl0X, mControl0Y;
    float mControl1X, mControl1Y;
    int mOperation;



    private PathPoint(int operation, float x, float y) {
        mOperation = operation;
        mX = x;
        mY = y;
    }

    private PathPoint(float c0X, float c0Y, float c1X, float c1Y, float x, float y) {
        mControl0X = c0X;
        mControl0Y = c0Y;
        mControl1X = c1X;
        mControl1Y = c1Y;
        mX = x;
        mY = y;
        mOperation = CURVE;
    }

    public static PathPoint lineTo(float x, float y) {
        return new PathPoint(LINE, x, y);
    }
    public static PathPoint curveTo(float c0X, float c0Y, float c1X, float c1Y, float x, float y) {
        return new PathPoint(c0X,  c0Y, c1X, c1Y, x, y);
    }
    public static PathPoint moveTo(float x, float y) {
        return new PathPoint(MOVE, x, y);
    }
}
