package com.delitx.furnituremaster.data.network

sealed class DataLoadState<T> {
    class Undefined<T> : DataLoadState<T>()
    class Loading<T> : DataLoadState<T>()
    class Data<T>(val value: T) : DataLoadState<T>()
    class LoadingError<T> : DataLoadState<T>()
}
