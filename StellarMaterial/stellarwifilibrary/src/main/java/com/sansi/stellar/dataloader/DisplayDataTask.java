package com.sansi.stellar.dataloader;

final class DisplayDataTask implements Runnable {

	private static final String LOG_DISPLAY_IMAGE_IN_IMAGEAWARE = "Display image in ViewAware (loaded from %1$s) [%2$s]";
    private static final String LOG_TASK_CANCELLED_IMAGEAWARE_REUSED = "ViewAware is reused for another image. Task is cancelled. [%s]";
	private static final String LOG_TASK_CANCELLED_IMAGEAWARE_COLLECTED = "ViewAware was collected by GC. Task is cancelled. [%s]";

	private final Object data;
	private final ViewAware viewAware;
	private final String memoryCacheKey;
	private final AsyncDataLoader engine;
	private final LoadedFrom loadedFrom;

	public DisplayDataTask(Object data, ViewAware viewAware, AsyncDataLoader dataLoader, String memoryCacheKey, LoadedFrom loadedFrom) {
		this.data = data;
		this.viewAware = viewAware;
		this.memoryCacheKey = memoryCacheKey;
		this.engine = dataLoader;
		this.loadedFrom=loadedFrom;
	}

	@Override
	public void run() {
		if (viewAware.isCollected()) {
			L.d(LOG_TASK_CANCELLED_IMAGEAWARE_COLLECTED, memoryCacheKey);
		} else if (isViewWasReused()) {
			L.d(LOG_TASK_CANCELLED_IMAGEAWARE_REUSED, memoryCacheKey);
		} else {
			L.d(LOG_DISPLAY_IMAGE_IN_IMAGEAWARE, loadedFrom, memoryCacheKey);
			viewAware.setData(data);
			engine.cancelDisplayTaskFor(viewAware);
		}
	}

	/** Checks whether memory cache key  for current ViewAware is actual */
	private boolean isViewWasReused() {
		String currentCacheKey = engine.getLoadingUriForView(viewAware);
		return !memoryCacheKey.equals(currentCacheKey);
	}
}