/**
 * Description: Interface TokenStream coping a folder to the destination folder.
 * @author Giang Truong GiangTruong321@yahoo.com, 
 * 		   Jian Guo jianguo0808@gmail.com,
 * 		   Wanli Cheng chengwl2008@gmail.com. 
 */
public interface TokenStream {
   /**
    Returns the next token from the stream, or null if there is no token
    available.
    */
   String nextToken();

   /**
    Returns true if the stream has tokens remaining.
    */
   boolean hasNextToken();
}
