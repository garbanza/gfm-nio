package com.ferremundo;

import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class FileWatcher extends Thread {
	private File file;
	private AtomicBoolean stop = new AtomicBoolean(false);
	private DoOnChangeFile doOnChangeFile;

	public FileWatcher(File file,DoOnChangeFile doOnChangeFile) {
		this.file = file;
		this.doOnChangeFile=doOnChangeFile;
	}

	public boolean isStopped() {
		return stop.get();
	}

	public void stopThread() {
		stop.set(true);
	}

	public interface DoOnChangeFile {
		public void execute();
	}

	@Override
	public void run() {
		try (WatchService watcher = FileSystems.getDefault().newWatchService()) {
			Path path = file.toPath().getParent();
			path.register(watcher, StandardWatchEventKinds.ENTRY_MODIFY);
			while (!isStopped()) {
				WatchKey key;
				try {
					key = watcher.poll(60*5, TimeUnit.SECONDS);
				} catch (InterruptedException e) {
					return;
				}
				if (key == null) {
					Thread.yield();
					continue;
				}

				for (WatchEvent<?> event : key.pollEvents()) {
					WatchEvent.Kind<?> kind = event.kind();

					@SuppressWarnings("unchecked")
					WatchEvent<Path> ev = (WatchEvent<Path>) event;
					Path filename = ev.context();

					if (kind == StandardWatchEventKinds.OVERFLOW) {
						Thread.yield();
						continue;
					} else if (kind == java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY
							&& filename.toString().equals(file.getName())) {
						doOnChangeFile.execute();
					}
					boolean valid = key.reset();
					if (!valid) {
						break;
					}
				}
				Thread.yield();
			}
		} catch (Throwable e) {
			// Log or rethrow the error
		}
	}
	
	public static void main(String[] args) {
		FileWatcher fileWatcher=new FileWatcher(new File("somefile"), new DoOnChangeFile() {
			@Override
			public void execute() {
				System.out.println("changed!!");
			}
		});
		fileWatcher.run();
	}
}
