/*
 * Copyright (c) 2016, Uber Technologies, Inc
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.uber.jaeger.samplers;

public class ProbabilisticSampler implements Sampler {
    private final long positiveSamplingBoundary;
    private final long negativeSamplingBoundary;
    private final double samplingRate;

    public ProbabilisticSampler(double samplingRate) {
        if (samplingRate < 0.0 || samplingRate > 1.0) {
            throw new IllegalArgumentException("The sampling rate must be greater than 0.0 and less than 1.0");
        }

        this.samplingRate = samplingRate;
        this.positiveSamplingBoundary = (long) (((1L << 63) -1) * samplingRate);
        this.negativeSamplingBoundary = (long) ((1L << 63) * samplingRate);
    }

    /**
     * Uses a trace id to make a sampling decision.
     *
     * @param id A long that represents the traceid used to make a sampling decision
     * @return A boolean that says wheter or not to sample.
     */
    public boolean isSampled(long id) {
        if (id > 0) {
            return id <= this.positiveSamplingBoundary;
        }
        return id >= this.negativeSamplingBoundary;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other instanceof ProbabilisticSampler) {
            return this.samplingRate == ((ProbabilisticSampler) other).samplingRate;
        }
        return false;
    }

    /**
     * Only implemented to satisfy the sampler interface
     */
    public void close() {
        // nothing to do
    }
}