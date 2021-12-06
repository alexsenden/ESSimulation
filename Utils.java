
public class Utils {
	
	public static void quickSort(Individual[] arr) {
		quickSort(arr, 0, arr.length - 1);
	}
	
	private static void quickSort(Individual[] arr, int lo, int hi) {
		if (lo < hi) {
			int index = partition(arr, lo, hi);
			
			quickSort(arr, lo, index - 1);
			quickSort(arr, index + 1, hi);
		}
	}
	
	private static int partition(Individual[] arr, int lo, int hi) {
		Individual pivot = arr[hi]; 
		int i = (lo - 1); 
		
		for(int j = lo; j <= hi - 1; j++) {
			if (arr[j].compareTo(pivot) > 0) {
				i++; 
				swap(arr, i, j);
		    }
		}
		
		swap(arr, i + 1, hi);
		
		return (i + 1);
	}
	
	private static void swap(Individual[] arr, int i, int j) {
		Individual temp = arr[i];
		arr[i] = arr[j];
		arr[j] = temp;
	}
}
