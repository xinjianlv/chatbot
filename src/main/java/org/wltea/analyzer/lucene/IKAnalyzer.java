package org.wltea.analyzer.lucene;
 
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Tokenizer;
 
/**
 * @desc: 分词器对IK做了简单修改兼容Lucene5X
 */
public class IKAnalyzer extends Analyzer{
     
    private boolean useSmart;
     
    public boolean useSmart() {
        return useSmart;
    }
  
    public void setUseSmart(boolean useSmart) {
        this.useSmart = useSmart;
    }
  
    /**
     * IK分词器Lucene  Analyzer接口实现类
     * 
     * 默认细粒度切分算法
     */
    public IKAnalyzer(){
        this(false);
    }
      
    /**
     * IK分词器Lucene Analyzer接口实现类
     * 
     * @param useSmart 当为true时，分词器进行智能切分
     */
    public IKAnalyzer(boolean useSmart){
        super();
        this.useSmart = useSmart;
    }
  
      
    /**
    protected TokenStreamComponents createComponents(String fieldName, final Reader in) {
        Tokenizer _IKTokenizer = new IKTokenizer(in , this.useSmart());
        return new TokenStreamComponents(_IKTokenizer);
    }
    **/
      
      
    /**
     * 重写最新版本的createComponents
     * 重载Analyzer接口，构造分词组件
     */
    @Override
    protected TokenStreamComponents createComponents(String fieldName) {
        Tokenizer _IKTokenizer = new IKTokenizer(this.useSmart());
        return new TokenStreamComponents(_IKTokenizer);
    }
 
}
 
